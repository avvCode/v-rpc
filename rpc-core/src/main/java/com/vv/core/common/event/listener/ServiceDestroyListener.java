package com.vv.core.common.event.listener;

import com.vv.core.common.event.VRpcDestroyEvent;
import com.vv.core.common.event.VRpcListener;
import com.vv.core.registy.URL;

import static com.vv.core.common.cache.ServerCache.PROVIDER_URL_SET;
import static com.vv.core.common.cache.ServerCache.REGISTRY_SERVICE;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/24-15:47
 */
public class ServiceDestroyListener implements VRpcListener<VRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}