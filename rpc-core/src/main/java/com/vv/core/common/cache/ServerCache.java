package com.vv.core.common.cache;

import com.vv.core.registy.RegistryService;
import com.vv.core.registy.URL;
import com.vv.core.serialize.SerializeFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
}
