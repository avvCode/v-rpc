package com.vv.core.config;

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
     * 负载均衡策略 example:random,rotate
     */
    private String routerStrategy;
}
