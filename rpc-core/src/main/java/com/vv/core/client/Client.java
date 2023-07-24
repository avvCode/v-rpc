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
import com.vv.core.proxy.jdk.JDKProxyFactory;
import com.vv.core.registy.URL;
import com.vv.core.registy.zookeeper.AbstractRegister;
import com.vv.core.registy.zookeeper.ZookeeperRegister;
import com.vv.core.router.impl.RandomRouterImpl;
import com.vv.core.router.impl.RotateRouterImpl;
import com.vv.core.serialize.fastjson.FastJsonSerializeFactory;
import com.vv.core.serialize.hessian.HessianSerializeFactory;
import com.vv.core.serialize.jdk.JdkSerializeFactory;
import com.vv.core.serialize.kryo.KryoSerializeFactory;
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
import java.util.Map;

import static com.vv.core.common.cache.ClientCache.*;
import static com.vv.core.common.constant.RpcConstant.*;


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

    public Bootstrap getBootstrap() {
        return bootstrap;
    }


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
        if (JAVASSIST_PROXY_TYPE.equals(clientConfig.getProxyType())) {
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
        Map<String, String> result = abstractRegister.getServiceWeightMap(serviceBean.getName());
        URL_MAP.put(serviceBean.getName(),result);
        abstractRegister.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (URL providerURL : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerURL.getServiceName());
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerURL.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    logger.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.addParameter("servicePath",providerURL.getServiceName()+"/provider");
            url.addParameter("providerIps", JSON.toJSONString(providerIps));
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
                    RpcInvocation data = SEND_QUEUE.take();
                    RpcProtocol rpcProtocol = new RpcProtocol(CLIENT_SERIALIZE_FACTORY.serialize(data));
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 后续可以考虑加入spi
     */
    private void initClientConfig() {
        //初始化路由策略
        String routerStrategy = clientConfig.getRouterStrategy();
        switch (routerStrategy) {
            case RANDOM_ROUTER_TYPE:
                IROUTER = new RandomRouterImpl();
                break;
            case ROTATE_ROUTER_TYPE:
                IROUTER = new RotateRouterImpl();
                break;
            default:
                throw new RuntimeException("no match routerStrategy for" + routerStrategy);
        }
        String clientSerialize = clientConfig.getClientSerialize();
        switch (clientSerialize) {
            case JDK_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new JdkSerializeFactory();
                break;
            case FAST_JSON_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new FastJsonSerializeFactory();
                break;
            case HESSIAN2_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new HessianSerializeFactory();
                break;
            case KRYO_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new KryoSerializeFactory();
                break;
            default:
                throw new RuntimeException("no match serialize type for " + clientSerialize);
        }
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        client.initClientConfig();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
