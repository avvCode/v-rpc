package com.vv.core.proxy.javassist;

import com.vv.core.client.RpcReferenceWrapper;
import com.vv.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @author vv
 * @Description
 * @date 2023/7/21-17:58
 */
public class JavassistProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) Proxy.newProxyInstance(rpcReferenceWrapper.getAimClass().getClassLoader(), new Class[]{rpcReferenceWrapper.getAimClass()},
                new JavassistInvocationHandler(rpcReferenceWrapper));
    }
}