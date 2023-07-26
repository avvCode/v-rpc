package com.vv.core.filter;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.filter.IFilter;

import java.util.List;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-14:59
 */
public interface IClientFilter extends IFilter {

    /**
     * 执行过滤链
     *
     * @param src
     * @param rpcInvocation
     * @return
     */
    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);
}