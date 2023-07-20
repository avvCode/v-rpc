package com.vv.core.server;

import com.vv.core.client.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vv
 * @Description 服务端启动类
 * @date 2023/7/20-17:39
 */
@Data
public class Server {
    private Logger logger = LoggerFactory.getLogger(Server.class);
    private ServerConfig serverConfig;
    public static void main(String[] args) {
        Server server = new Server();
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(8080);
        server.setServerConfig(serverConfig);
        server.start();
    }
    public void start(){
        logger.info("==== 服务端启动中 ====");
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // 3
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() { // 6
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(serverConfig.getPort());
        logger.info("==== 服务端启动成功 ====");
        logger.info("==== 监听端口：{} ====",serverConfig.getPort());
    }
}
