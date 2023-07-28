package com.vv.core.common.exception;

import com.vv.core.common.RpcInvocation;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/28-16:16
 */
public class IRpcException extends RuntimeException {

    private RpcInvocation rpcInvocation;

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

    public IRpcException(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

}