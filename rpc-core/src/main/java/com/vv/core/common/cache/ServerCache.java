package com.vv.core.common.cache;

import com.vv.core.common.ServerServiceSemaphoreWrapper;
import com.vv.core.common.config.ServerConfig;
import com.vv.core.dispatcher.ServerChannelDispatcher;
import com.vv.core.filter.server.ServerBeforeFilterChain;
import com.vv.core.filter.server.ServerAfterFilterChain;
import com.vv.core.registry.URL;
import com.vv.core.registry.zookeeper.AbstractRegister;
import com.vv.core.serialize.SerializeFactory;
import com.vv.core.server.ServiceWrapper;
import io.netty.util.internal.ConcurrentSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description 存储服务
 * @date 2023/7/21-13:29
 */
public class ServerCache {
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new ConcurrentSet<>();
    public static AbstractRegister REGISTRY_SERVICE;
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;
    public static ServerConfig SERVER_CONFIG;
    public static ServerBeforeFilterChain SERVER_BEFORE_FILTER_CHAIN;
    public static ServerAfterFilterChain SERVER_AFTER_FILTER_CHAIN;
    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();
    public static Boolean IS_STARTED = false;
    public static ServerChannelDispatcher SERVER_CHANNEL_DISPATCHER = new ServerChannelDispatcher();
    public static final Map<String, ServerServiceSemaphoreWrapper> SERVER_SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>(64);
}
