package com.vv.core.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/24-14:37
 */
public class ChannelFuturePollingRef {
    private AtomicLong referenceTimes = new AtomicLong(0);


    public ChannelFutureWrapper getChannelFutureWrapper(ChannelFutureWrapper[] arr){
//        ChannelFutureWrapper[] arr = SERVICE_ROUTER_MAP.get(serviceName);
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }
}
