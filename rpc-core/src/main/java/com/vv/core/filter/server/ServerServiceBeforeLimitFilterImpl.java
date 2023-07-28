package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;
import com.vv.core.common.ServerServiceSemaphoreWrapper;
import com.vv.core.common.annotation.SPI;
import com.vv.core.common.exception.MaxServiceLimitRequestException;
import com.vv.core.filter.IServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

import static com.vv.core.common.cache.ServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/28-16:52
 */
@SPI("before")
public class ServerServiceBeforeLimitFilterImpl implements IServerFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerServiceBeforeLimitFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        //从缓存中提取semaphore对象
        Semaphore semaphore = serverServiceSemaphoreWrapper.getSemaphore();
        boolean tryResult = semaphore.tryAcquire();
        if (!tryResult) {
            LOGGER.error("[ServerServiceBeforeLimitFilterImpl] {}'s max request is {},reject now", rpcInvocation.getTargetServiceName(), serverServiceSemaphoreWrapper.getMaxNums());
            MaxServiceLimitRequestException iRpcException = new MaxServiceLimitRequestException(rpcInvocation);
            rpcInvocation.setE(iRpcException);
            throw iRpcException;
        }
    }
}