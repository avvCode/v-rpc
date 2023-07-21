package com.vv.core.common.cache;

import com.vv.core.common.RpcInvocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/21-13:53
 */
public class ClientCache {
    /**
     * 任务队列
     */
    public static final BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    /**
     * 结果队列
     */
    public static final Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();
}
