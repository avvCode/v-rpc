package com.vv.core.client.proxy.javassist;

import com.vv.core.client.proxy.ProxyFactory;

/**
 * @author vv
 * @Description
 * @date 2023/7/21-17:58
 */
public class JavassistProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class clazz) throws Exception {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                clazz, new JavassistInvocationHandler(clazz));
    }
}