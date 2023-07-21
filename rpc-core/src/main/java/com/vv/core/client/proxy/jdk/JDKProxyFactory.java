package com.vv.core.client.proxy.jdk;

import com.vv.core.client.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @author vv
 * @Description
 * @date 2023/7/21-14:46
 */
public class JDKProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(final Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new JDKInvocationHandler(clazz));
    }
}
