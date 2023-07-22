package com.vv.core.common;

import com.vv.core.client.ClientConfig;
import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.registy.URL;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description 客户端缓存
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

    public static ClientConfig CLIENT_CONFIG;

    //provider名称 --> 该服务有哪些集群URL
    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();

    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    //每次进行远程调用的时候都是从这里面去选择服务提供者

    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();


}
