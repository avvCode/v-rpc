package com.vv.core.proxy.jdk;

import com.vv.core.client.RpcReferenceWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.cache.ClientCache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author vv
 * @Description JDK代理类实现
 * @date 2023/7/21-14:42
 */
public class JDKInvocationHandler implements InvocationHandler {
    private static final Object OBJECT = new Object();
    /**
     * 被代理的对象
     */
    private Class target;

    public JDKInvocationHandler(Class target) {
        this.target = target;
    }
    private RpcReferenceWrapper rpcReferenceWrapper;

    public JDKInvocationHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(target.getName());
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        ClientCache.RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        ClientCache.SEND_QUEUE.add(rpcInvocation);

        long beginTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - beginTime < 3*1000) {
            Object object = ClientCache.RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                return ((RpcInvocation)object).getResponse();
            }
        }
        throw new TimeoutException("client wait server's response timeout!");
    }
}
