package com.vv.core.server;

import com.vv.core.common.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * @author vv
 * @Description 从客户端接收到的任务
 * @date 2023/7/27-15:11
 */
@Data
public class ServerChannelReadData {
    private RpcProtocol rpcProtocol;

    private ChannelHandlerContext channelHandlerContext;
}
