package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-15:01
 */
public class ServerFilterChain {
    private static List<IServerFilter> iServerFilters = new ArrayList<>();

    public void addServerFilter(IServerFilter iServerFilter) {
        iServerFilters.add(iServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (IServerFilter iServerFilter : iServerFilters) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }
}
