package com.vv.core.client;

import com.vv.core.common.RpcInvocation;
import com.vv.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.core.common.cache.ClientCache.CLIENT_SERIALIZE_FACTORY;
import static com.vv.core.common.cache.ClientCache.RESP_MAP;


/**
 * @author vv
 * @Description 客户端接收到请求后的处理方式
 * @date 2023/7/20-19:09
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("收到服务端响应：{}",msg);
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        byte[] reqContent = rpcProtocol.getContent();
        RpcInvocation rpcInvocation = CLIENT_SERIALIZE_FACTORY.deserialize(reqContent, RpcInvocation.class);
        //接收到到服务端消息，发现是异常，直接打印出来
        if (rpcInvocation.getE() != null) {
            rpcInvocation.getE().printStackTrace();
        }
        //如果是单纯异步模式的话，响应Map集合中不会存在映射值
        Object r = rpcInvocation.getAttachments().get("async");
        if (r != null && Boolean.valueOf(String.valueOf(r))) {
            ReferenceCountUtil.release(msg);
            return;
        }
        if (!RESP_MAP.containsKey(rpcInvocation.getUuid())) {
            throw new IllegalArgumentException("server response is error!");
        }
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
