package com.vv.core.client;

import com.alibaba.fastjson.JSON;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.core.common.ClientCache.RESP_MAP;


/**
 * @author vv
 * @Description 客户端接收到请求后的处理方式
 * @date 2023/7/20-19:09
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol protocol = (RpcProtocol) msg;
        String json = new String(protocol.getContent(),0, protocol.getContentLength());
        RpcInvocation rpcInvocation = JSON.parseObject(json, RpcInvocation.class);
        logger.info("收到服务端响应：{}",rpcInvocation);
        if(!RESP_MAP.containsKey(rpcInvocation.getUuid())){
            throw new IllegalArgumentException("server response is error!");
        }
        RESP_MAP.put(rpcInvocation.getUuid(),rpcInvocation);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()){
            ctx.close();
        }
    }
}
