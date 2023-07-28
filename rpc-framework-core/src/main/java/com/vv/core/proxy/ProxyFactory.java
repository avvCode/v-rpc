package com.vv.core.proxy;

import com.vv.core.client.RpcReferenceWrapper;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/21-14:44
 */
public interface ProxyFactory {
    /**
     * 获取代理对象
     * @return
     * @param <T>
     */
    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;
}
