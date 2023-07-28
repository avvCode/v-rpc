package com.vv.core.router.impl;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.registry.URL;
import com.vv.core.router.IRouter;
import com.vv.core.router.Selector;

import java.util.List;

import static com.vv.core.common.cache.ClientCache.*;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/24-14:39
 */
public class RotateRouterImpl implements IRouter {
    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i=0;i<channelFutureWrappers.size();i++) {
            arr[i]=channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(),arr);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getChannelFutureWrappers());
    }

    @Override
    public void updateWeight(URL url) {

    }
}
