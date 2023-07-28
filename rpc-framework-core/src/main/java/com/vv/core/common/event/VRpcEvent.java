package com.vv.core.common.event;

/**
 * @author vv
 * @Description 事件对象
 * @date 2023/7/23-16:01
 */
public interface VRpcEvent {

    Object getData();

    VRpcEvent setData(Object data);
}
