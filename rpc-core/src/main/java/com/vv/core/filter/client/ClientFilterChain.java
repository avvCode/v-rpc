package com.vv.core.filter.client;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-15:01
 */
public class ClientFilterChain {
    private static List<IClientFilter> iClientFilterList = new ArrayList<>();

    public void addClientFilter(IClientFilter iClientFilter) {
        iClientFilterList.add(iClientFilter);
    }

    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for (IClientFilter iClientFilter : iClientFilterList) {
            iClientFilter.doFilter(src, rpcInvocation);
        }
    }
}
