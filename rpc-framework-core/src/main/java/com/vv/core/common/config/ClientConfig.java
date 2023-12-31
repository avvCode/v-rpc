package com.vv.core.common.config;

import lombok.Data;

/**
 * @author vv
 * @Description 客户端配置类
 * @date 2023/7/20-17:51
 */
@Data
public class ClientConfig {
    /**
     * 服务端端口
     */
    private String applicationName;

    /**
     * 注册服务地址
     */
    private String registerAddr;

    /**
     * 代理实现方式
     */
    private String proxyType;

    /**
     * 注册中心类型
     */
    private String registerType;

    /**
     * 负载均衡策略 example:random,rotate
     */
    private String routerStrategy;

    /**
     * 客户端序列化方式 example: hession2,kryo,jdk,fastjson
     */
    private String clientSerialize;

    /**
     * 客户端发数据的超时时间
     */
    private Integer timeOut;

    /**
     * 客户端最大响应数据体积
     */
    private Integer maxServerRespDataSize;
}
