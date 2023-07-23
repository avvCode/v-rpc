package com.vv.core.common.event;

/**
 * @author vv
 * @Description 事件更新对象
 * @date 2023/7/23-16:03
 */
public class VRpcUpdateEvent implements VRpcEvent{

    private Object data;

    public VRpcUpdateEvent(Object data) {
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
