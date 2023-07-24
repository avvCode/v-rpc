package com.vv.core.common.event;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.registy.URL;
import com.vv.core.registy.zookeeper.ProviderNodeInfo;

import java.util.List;

import static com.vv.core.common.cache.ClientCache.CONNECT_MAP;
import static com.vv.core.common.cache.ClientCache.IROUTER;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/24-15:44
 */
public class ProviderNodeDataChangeListener implements VRpcListener<VRpcNodeChangeEvent>{
    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrappers =  CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            String address = channelFutureWrapper.getHost()+":"+channelFutureWrapper.getPort();
            if(address.equals(providerNodeInfo.getAddress())){
                //修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                //更新权重
                IROUTER.updateWeight(url);
                break;
            }
        }
    }
}
