package com.vv.core.filter.client;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.utils.CommonUtils;
import com.vv.core.filter.IClientFilter;

import java.util.List;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-17:56
 */
public class GroupFilterImpl implements IClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = String.valueOf(rpcInvocation.getAttachments().get("group"));
        for (ChannelFutureWrapper channelFutureWrapper : src) {
            if (!group.equals(channelFutureWrapper.getGroup())) {
                src.remove(channelFutureWrapper);
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            throw new RuntimeException("no provider match for group " + group);
        }
    }
}