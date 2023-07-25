package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-14:59
 */
public interface IServerFilter {
    /**
     * 执行核心过滤逻辑
     *
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);
}
