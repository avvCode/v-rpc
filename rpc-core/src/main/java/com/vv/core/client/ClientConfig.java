package com.vv.core.client;

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
    private int port;

    /**
     * 服务端地址
     */
    private String serverAddr;
}
