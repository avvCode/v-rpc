package com.vv.core.client;

import com.vv.core.proxy.ProxyFactory;

/**
 * @author vv
 * @Description 封装多一层代理
 * @date 2023/7/21-15:04
 */
public class RpcReference {
    private ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }
    /**
     * 根据接口类型获取代理对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    /**
     * 根据接口类型获取代理对象
     *
     * @param rpcReferenceWrapper
     * @param <T>
     * @return
     */
    public <T> T get(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        return proxyFactory.getProxy(rpcReferenceWrapper);
    }
}
