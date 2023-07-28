package com.vv.core.common.event;

import com.vv.core.common.event.listener.ProviderNodeDataChangeListener;
import com.vv.core.common.event.listener.ServiceDestroyListener;
import com.vv.core.common.event.listener.ServiceUpdateListener;
import com.vv.core.common.event.listener.VRpcListener;
import com.vv.core.common.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/23-16:07
 */
public class VRpcListenerLoader {
    private static List<VRpcListener> vRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(VRpcListener iRpcListener) {
        vRpcListenerList.add(iRpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
        registerListener(new ServiceDestroyListener());
        registerListener(new ProviderNodeDataChangeListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o 接口
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    /**
     * 同步事件处理，可能会堵塞
     *
     * @param iRpcEvent
     */
    public static void sendSyncEvent(VRpcEvent iRpcEvent) {
        System.out.println(vRpcListenerList);
        if (CommonUtils.isEmptyList(vRpcListenerList)) {
            return;
        }
        for (VRpcListener<?> vRpcListener : vRpcListenerList) {
            Class<?> type = getInterfaceT(vRpcListener);
            if (type.equals(iRpcEvent.getClass())) {
                try {
                    vRpcListener.callBack(iRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendEvent(VRpcEvent iRpcEvent) {
        if (CommonUtils.isEmptyList(vRpcListenerList)) {
            return;
        }
        for (VRpcListener<?> vRpcListener : vRpcListenerList) {
            Class<?> type = getInterfaceT(vRpcListener);
            if (type.equals(iRpcEvent.getClass())) {
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            vRpcListener.callBack(iRpcEvent.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
