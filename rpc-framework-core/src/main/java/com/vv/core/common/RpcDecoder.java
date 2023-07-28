package com.vv.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.vv.core.common.constant.RpcConstant.MAGIC_NUMBER;

/**
 * @author vv
 * @Description 编码器
 * @date 2023/7/20-18:49
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     /**
     * 协议的开头部分的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out)  {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            if (!(byteBuf.readShort() == MAGIC_NUMBER)) {
                ctx.close();
                return;
            }
            int length = byteBuf.readInt();
            if (byteBuf.readableBytes() < length) {
                //数据包有异常
                ctx.close();
                return;
            }
            byte[] body = new byte[length];
            byteBuf.readBytes(body);
            RpcProtocol rpcProtocol = new RpcProtocol(body);
            out.add(rpcProtocol);
        }
    }
}