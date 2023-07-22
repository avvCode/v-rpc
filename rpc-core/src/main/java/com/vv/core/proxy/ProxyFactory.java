package com.vv.core.proxy;

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
    <T> T getProxy(final Class clazz) throws Exception;
}
