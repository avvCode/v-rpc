package com.vv.core.common.cache;

import com.vv.core.common.ChannelFuturePollingRef;
import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.config.ClientConfig;
import com.vv.core.registy.URL;
import com.vv.core.router.IRouter;

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
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(100);
    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();
    //provider名称 --> 该服务有哪些集群URL
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    //com.vv.test.service -> <<ip:host,urlString>,<ip:host,urlString>,<ip:host,urlString>>
    public static Map<String, Map<String,String>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    //每次进行远程调用的时候都是从这里面去选择服务提供者
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
    //随机请求的map
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    public static IRouter IROUTER;

}
