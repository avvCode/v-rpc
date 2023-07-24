package com.vv.core.common.event;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/24-15:38
 */
public class VRpcNodeChangeEvent implements VRpcEvent{
    private Object data;

    public VRpcNodeChangeEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public VRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
