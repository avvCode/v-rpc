package com.vv.core.filter.client;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.common.RpcInvocation;
import com.vv.core.filter.IClientFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.vv.core.common.cache.ClientCache.CLIENT_CONFIG;

/**
 * @author vv
 * @Description TODO
 * @date 2023/7/25-17:55
 */
public class ClientLogFilterImpl implements IClientFilter {
    private static Logger logger = LoggerFactory.getLogger(ClientLogFilterImpl.class);

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("c_app_name",CLIENT_CONFIG.getApplicationName());
        logger.info(rpcInvocation.getAttachments().get("c_app_name")+" do invoke -----> "+rpcInvocation.getTargetServiceName());
    }
}
