package com.vv.core.common.config;

import lombok.Data;

/**
 * @author vv
 * @Description 服务端配置类
 * @date 2023/7/20-17:52
 */
@Data
public class ServerConfig {
    /**
     * 监听端口号
     */
    private Integer serverPort;

    /**
     * 注册地址
     */
    private String registerAddr;

    /**
     * 服务名称
     */
    private String applicationName;

    /**
     * 服务端序列化方式 example: hession2,kryo,jdk,fastjson
     */
    private String serverSerialize;

    private String registerType;

    /**
     * 服务端业务线程数目
     */
    private Integer serverBizThreadNums;

    /**
     * 服务端接收队列的大小
     */
    private Integer serverQueueSize;


    /**
     * 限制服务端最大所能接受的数据包体积
     */
    private Integer maxServerRequestData;

    /**
     * 服务端最大连接数
     */
    private Integer maxConnections;
}
