package com.vv.core.common.cache;

import com.vv.core.common.config.ServerConfig;
import com.vv.core.filter.server.ServerFilterChain;
import com.vv.core.registry.RegistryService;
import com.vv.core.registry.URL;
import com.vv.core.serialize.SerializeFactory;
import com.vv.core.server.ServiceWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description 存储服务
 * @date 2023/7/21-13:29
 */
public class ServerCache {
    /**
     * 服务提供类
     */
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new HashMap<>();
    /**
     * 服务提供的URL
     */
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();

    public static RegistryService REGISTRY_SERVICE;

    public static SerializeFactory SERVER_SERIALIZE_FACTORY;

    public static ServerConfig SERVER_CONFIG;

    public static ServerFilterChain SERVER_FILTER_CHAIN;

    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();
}
