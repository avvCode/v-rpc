package com.vv.core.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-21:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWrapper {
    /**
     * 对外暴露的具体服务对象
     */
    private Object serviceObj;

    /**
     * 具体暴露服务的分组
     */
    private String group = "default";

    /**
     * 整个应用的token校验
     */
    private String serviceToken = "";

    /**
     * 限流策略
     */
    private Integer limit = -1;
    public ServiceWrapper(Object serviceObj, String group) {
        this.serviceObj = serviceObj;
        this.group = group;
    }

}
