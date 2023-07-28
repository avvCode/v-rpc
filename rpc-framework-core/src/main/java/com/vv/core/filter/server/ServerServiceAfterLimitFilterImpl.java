package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;
import com.vv.core.common.ServerServiceSemaphoreWrapper;
import com.vv.core.common.annotation.SPI;
import com.vv.core.filter.IServerFilter;

import static com.vv.core.common.cache.ServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/28-16:52
 */
@SPI("after")
public class ServerServiceAfterLimitFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        serverServiceSemaphoreWrapper.getSemaphore().release();
    }
}