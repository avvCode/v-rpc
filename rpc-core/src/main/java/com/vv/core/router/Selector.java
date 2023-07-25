package com.vv.core.router;

import com.vv.core.common.ChannelFutureWrapper;
import lombok.Data;

/**
 * @author vv
 * @Description 选择服务
 * @date 2023/7/24-14:14
 */
@Data
public class Selector {
    private String providerServiceName;
    /**
     * 经过二次筛选之后的future集合
     */
    private ChannelFutureWrapper[] channelFutureWrappers;
}
