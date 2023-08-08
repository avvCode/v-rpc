package com.vv.core.common.cache;

import com.vv.core.common.ChannelFuturePollingRef;
import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.config.ClientConfig;
import com.vv.core.filter.client.ClientFilterChain;
import com.vv.core.registry.URL;
import com.vv.core.registry.zookeeper.AbstractRegister;
import com.vv.core.router.IRouter;
import com.vv.core.serialize.SerializeFactory;
import com.vv.core.spi.ExtensionLoader;

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
     * 任务发送队列
     */
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(5000);
    /**
     * 结果队列
     */
    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();
    //provider名称 --> 该服务有哪些集群URL
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
    //com.sise.test.service -> <<ip:host,urlString>,<ip:host,urlString>,<ip:host,urlString>>
    public static Map<String, Map<String,String>> URL_MAP = new ConcurrentHashMap<>();
    public static Set<String> SERVER_ADDRESS = new HashSet<>();
    //每次进行远程调用的时候都是从这里面去选择服务提供者
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
    //随机请求的map
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();
    /**
     * 通道选择策略
     */
    public static IRouter IROUTER;
    /**
     * 序列化策略
     */
    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;
    /**
     * 客户端全局配置
     */
    public static ClientConfig CLIENT_CONFIG;
    /**
     * 调用链
     */
    public static ClientFilterChain CLIENT_FILTER_CHAIN;
    /**
     * 注册中心
     */
    public static AbstractRegister ABSTRACT_REGISTER;
    /**
     * spi机制扩展
     */
    public static ExtensionLoader EXTENSION_LOADER = new ExtensionLoader();

}
