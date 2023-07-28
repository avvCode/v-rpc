package com.vv.core.client;


public class RpcReferenceFuture<T> {

    private RpcReferenceWrapper rpcReferenceWrapper;

    private Object response;

    public RpcReferenceWrapper getRpcReferenceWrapper() {
        return rpcReferenceWrapper;
    }

    public void setRpcReferenceWrapper(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
