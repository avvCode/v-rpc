package com.vv.core.filter.client;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.common.utils.CommonUtils;
import com.vv.core.filter.IClientFilter;

import java.util.Iterator;
import java.util.List;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-21:57
 */
public class DirectInvokeFilterImpl implements IClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if(CommonUtils.isEmpty(url)){
            return;
        }
        Iterator<ChannelFutureWrapper> channelFutureWrapperIterator = src.iterator();
        while (channelFutureWrapperIterator.hasNext()){
            ChannelFutureWrapper channelFutureWrapper = channelFutureWrapperIterator.next();
            if(!(channelFutureWrapper.getHost()+":"+channelFutureWrapper.getPort()).equals(url)){
                channelFutureWrapperIterator.remove();
            }
        }
        if(CommonUtils.isEmptyList(src)){
            throw new RuntimeException("no match provider url for "+ url);
        }
    }
}
