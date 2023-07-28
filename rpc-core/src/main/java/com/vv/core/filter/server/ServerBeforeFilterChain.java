package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;
import com.vv.core.filter.IServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vv
 * @Description 后置过滤
 * @date 2023/7/28-16:48
 */
public class ServerBeforeFilterChain {
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
