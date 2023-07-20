package com.vv.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author vv
 * @Description 客户端启动类
 * @date 2023/7/20-17:39
 */
@Data
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);
    private ClientConfig clientConfig;

    public static void main(String[] args) throws InterruptedException {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setPort(8080);
        clientConfig.setServerAddr("localhost");
        Client client = new Client();
        client.setClientConfig(clientConfig);

        client.start();
    }
    public void start() throws InterruptedException {
        logger.info("==== 客户端启动中 ====");
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(clientConfig.getServerAddr(), clientConfig.getPort())
                .sync()
                .channel()
                .writeAndFlush("hello world!");
        logger.info("==== 客户端启动成功 ====");
    }
}
