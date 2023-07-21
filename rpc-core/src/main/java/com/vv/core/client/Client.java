package com.vv.core.client;

import com.alibaba.fastjson.JSON;
import com.vv.core.client.proxy.jdk.JDKProxyFactory;
import com.vv.core.common.RpcDecoder;
import com.vv.core.common.RpcEncoder;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.RpcProtocol;
import com.vv.core.common.cache.ClientCache;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import com.vv.interfaces.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author vv
 * @Description 客户端启动类
 * @date 2023/7/20-17:39
 */
@Data
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    private ClientConfig clientConfig;

    public static void main(String[] args) throws Throwable {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setPort(8080);
        clientConfig.setServerAddr("localhost");
        Client client = new Client();
        client.setClientConfig(clientConfig);
        Reference reference = client.startApplication();
        //获取代理对象
        DataService dataService = reference.get(DataService.class);

        String result = dataService.sendData("test");

        System.out.println(result);
    }
    public Reference startApplication() throws InterruptedException {
        logger.info("==== 客户端启动中 ====");
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new ClientHandler());
                    }
                })
                .connect(clientConfig.getServerAddr(), clientConfig.getPort())
                .sync();
        logger.info("==== 客户端启动成功 ====");
        this.startSendThread(channelFuture);
        Reference reference = new Reference(new JDKProxyFactory());
        return reference;
    }

    /**
     * 开启发送线程
     */
    public void startSendThread(ChannelFuture channelFuture){
        AsyncSendJob asyncSendJob = new AsyncSendJob(channelFuture);
        Thread sendThread = new Thread(asyncSendJob);
        sendThread.start();
    }
    class AsyncSendJob implements Runnable{
        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            logger.info("==== 启动发送线程 ====");
            logger.info("任务发送模式：阻塞获取...");
            while (true){
                try {
                    //阻塞拿任务
                    RpcInvocation task = ClientCache.SEND_QUEUE.take();
                    logger.info("拿到任务：{}",task);
                    String jsonString = JSON.toJSONString(task);
                    RpcProtocol rpcProtocol = new RpcProtocol(jsonString.getBytes());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
