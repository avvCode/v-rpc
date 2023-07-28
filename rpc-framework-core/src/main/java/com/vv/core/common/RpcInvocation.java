package com.vv.core.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vv
 * @Description 实际需要传输的类
 * @date 2023/7/20-18:58
 */
@Data
public class RpcInvocation implements Serializable {
    //请求的目标方法，例如findUser
    private String targetMethod;
    //请求的目标服务名称，例如：com.vv.user.UserService
    private String targetServiceName;
    //请求参数信息
    private Object[] args;
    private String uuid;
    //接口响应的数据塞入这个字段中（如果是异步调用或者void类型，这里就为空）
    private Object response;
    //额外数据，包含服务分组、服务token鉴权字段
    private Map<String, Object> attachments = new ConcurrentHashMap<>();
    //服务端异常
    private Throwable e;
    //超时重试次数
    private int retry;
}
