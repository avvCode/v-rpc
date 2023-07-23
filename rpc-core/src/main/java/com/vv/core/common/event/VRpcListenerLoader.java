package com.vv.core.common.event;

import com.vv.core.common.event.listener.ServiceUpdateListener;
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
    /**
     * 需要通知的服务列表
     */
    private static List<VRpcListener> VRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(VRpcListener vRpcListener) {
        VRpcListenerList.add(vRpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
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

    public static void sendEvent(VRpcEvent vRpcEvent) {
        if(CommonUtils.isEmptyList(VRpcListenerList)){
            return;
        }
        for (VRpcListener<?> vRpcListener : VRpcListenerList) {
            Class<?> type = getInterfaceT(vRpcListener);
            if(type.equals(vRpcEvent.getClass())){
                eventThreadPool.execute(() -> {
                    try {
                        vRpcListener.callBack(vRpcEvent.getData());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
