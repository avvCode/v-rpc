package com.vv.core.common.exception;

import com.vv.core.common.RpcInvocation;

/**
 * @author vv
 * @Description 连接到服务端的连接数过多
 * @date 2023/7/28-16:44
 */
public class MaxServiceLimitRequestException extends IRpcException{

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}
