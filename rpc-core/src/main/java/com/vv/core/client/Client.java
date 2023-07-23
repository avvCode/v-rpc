package com.vv.core.client;

import com.alibaba.fastjson.JSON;
import com.vv.core.common.event.VRpcListenerLoader;
import com.vv.core.common.utils.CommonUtils;
import com.vv.core.config.ClientConfig;
import com.vv.core.config.PropertiesBootstrap;
import com.vv.core.proxy.javassist.JavassistProxyFactory;
import com.vv.core.common.RpcDecoder;
import com.vv.core.common.RpcEncoder;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.RpcProtocol;
import com.vv.core.common.cache.ClientCache;
import com.vv.core.proxy.jdk.JDKProxyFactory;
import com.vv.core.registy.URL;
import com.vv.core.registy.zookeeper.AbstractRegister;
import com.vv.core.registy.zookeeper.ZookeeperRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import com.vv.interfaces.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.vv.core.common.cache.ClientCache.SUBSCRIBE_SERVICE_LIST;


/**
 * @author vv
 * @Description 客户端启动类
 * @date 2023/7/20-17:39
 */
@Data
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private VRpcListenerLoader vRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();


    public RpcReference initClientApplication() {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        vRpcListenerLoader = new VRpcListenerLoader();
        vRpcListenerLoader.init();
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        RpcReference rpcReference;
        if ("javassist".equals(clientConfig.getProxyType())) {
            rpcReference = new RpcReference(new JavassistProxyFactory());
        } else {
            rpcReference = new RpcReference(new JDKProxyFactory());
        }
        return rpcReference;
    }

    /**
     * 启动服务之前需要预先订阅对应的dubbo服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        if (abstractRegister == null) {
            abstractRegister = new ZookeeperRegister(clientConfig.getRegisterAddr());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        abstractRegister.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (String providerServiceName : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerServiceName);
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerServiceName, providerIp);
                } catch (InterruptedException e) {
                    logger.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.setServiceName(providerServiceName);
            abstractRegister.doAfterSubscribe(url);
        }
    }


    /**
     * 开启发送线程
     *
     * @param
     */
    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    class AsyncSendJob implements Runnable {

        public AsyncSendJob() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcInvocation data = ClientCache.SEND_QUEUE.take();
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();

        RpcReference rpcReference = client.initClientApplication();

        DataService dataService = rpcReference.get(DataService.class);


        client.doSubscribeService(DataService.class);

        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();

        for (int i = 0; i < 100; i++) {
            try {
                String result = dataService.sendData("test");
                System.out.println(result);
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
