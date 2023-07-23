package com.vv.core.common;

import io.netty.channel.ChannelFuture;
import lombok.Data;

/**
 * @author vv
 * @Description 保存连接，方便做选择策略
 * @date 2023/7/22-22:38
 */
@Data
public class ChannelFutureWrapper {
    private ChannelFuture channelFuture;

    private String host;

    private Integer port;
}