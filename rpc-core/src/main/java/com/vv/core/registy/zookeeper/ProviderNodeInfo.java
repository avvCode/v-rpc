package com.vv.core.registy.zookeeper;

import lombok.Data;

/**
 * @author vv
 * @Description 单个节点信息
 * @date 2023/7/22-21:55
 */
@Data
public class ProviderNodeInfo {
    private String applicationName;

    private String serviceName;

    private String address;

    private Integer weight;

    private String registryTime;

    private String group;
}
