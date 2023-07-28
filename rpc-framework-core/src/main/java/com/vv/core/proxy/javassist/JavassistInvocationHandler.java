package com.vv.core.proxy.javassist;



import com.vv.core.client.RpcReferenceWrapper;
import com.vv.core.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.vv.core.common.cache.ClientCache.RESP_MAP;
import static com.vv.core.common.cache.ClientCache.SEND_QUEUE;
import static com.vv.core.common.constant.RpcConstant.DEFAULT_TIMEOUT;

/**
 * @author vv
 * @Description Javassist代理类实现
 * @date 2023/7/21-17:58
 */

public class JavassistInvocationHandler implements InvocationHandler {


    private final static Object OBJECT = new Object();

    private RpcReferenceWrapper rpcReferenceWrapper;

    private Long timeOut = Long.valueOf(DEFAULT_TIMEOUT);

    public JavassistInvocationHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        timeOut = Long.valueOf(String.valueOf(rpcReferenceWrapper.getAttachments().get("timeOut")));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttachments());
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        rpcInvocation.setRetry(rpcReferenceWrapper.getRetry());

        SEND_QUEUE.add(rpcInvocation);
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        long beginTime = System.currentTimeMillis();
        int retryTimes = 0;
        while (System.currentTimeMillis() - beginTime < timeOut || rpcInvocation.getRetry() > 0) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                RpcInvocation rpcInvocationResp = (RpcInvocation) object;
                //正常结果
                if (rpcInvocationResp.getRetry() == 0 && rpcInvocationResp.getE() == null) {
                    return rpcInvocationResp.getResponse();
                } else if (rpcInvocationResp.getE() != null) {
                    //每次重试之后都会将retry值扣减1
                    if (rpcInvocationResp.getRetry() == 0) {
                        return rpcInvocationResp.getResponse();
                    }
                    //如果是因为超时的情况，才会触发重试规则，否则重试机制不生效
                    if (System.currentTimeMillis() - beginTime > timeOut) {
                        retryTimes++;
                        //重新请求
                        rpcInvocation.setResponse(null);
                        rpcInvocation.setRetry(rpcInvocationResp.getRetry() - 1);
                        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                        SEND_QUEUE.add(rpcInvocation);
                    }
                }
            }
        }
        throw new TimeoutException("Wait for response from server on client " + timeOut + "ms,retry times is " + retryTimes + ",service's name is " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}
