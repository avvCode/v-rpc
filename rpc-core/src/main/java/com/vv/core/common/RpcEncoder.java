package com.vv.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author vv
 * @Description 解码器
 * @date 2023/7/20-18:49
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol msg, ByteBuf out) {
        out.writeShort(msg.getMagicNumber());
        out.writeInt(msg.getContentLength());
        out.writeBytes(msg.getContent());
    }
}
