package com.vv.core.server;

import com.vv.core.common.RpcDecoder;
import com.vv.core.common.RpcEncoder;
import com.vv.core.common.cache.ServerCache;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
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
        server.registerService(new DataServiceImpl());
        server.start();
    }
    public void start(){
        logger.info("==== 服务端启动中 ====");
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new ServerHandler());
                    }
                })
                .bind(serverConfig.getPort());
        logger.info("==== 服务端启动成功 ====");
        logger.info("==== 监听端口：{} ====",serverConfig.getPort());
    }

    /**
     * 注册服务
     * @param serviceBean
     */
    public void registerService(Object serviceBean){
        //只能存在一个共同消费类
        Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if(interfaces.length != 1){
            throw new RuntimeException("接口太少或太多");
        }
        Class<?> anInterface = interfaces[0];
        String interfaceName = anInterface.getName();
        ServerCache.PROVIDER_CLASS_MAP.put(interfaceName,serviceBean);

    }
}
