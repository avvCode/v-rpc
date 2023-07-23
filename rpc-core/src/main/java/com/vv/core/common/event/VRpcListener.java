package com.vv.core.common.event;

/**
 * @author vv
 * @Description 监听器，当某个节点变更时会发送一个事件
 * @date 2023/7/23-16:05
 */
public interface VRpcListener<T> {
    void callBack(Object t);
}
