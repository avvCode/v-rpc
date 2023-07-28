package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;
import com.vv.core.common.annotation.SPI;
import com.vv.core.common.utils.CommonUtils;
import com.vv.core.filter.IServerFilter;
import com.vv.core.server.ServiceWrapper;

import static com.vv.core.common.cache.ServerCache.PROVIDER_SERVICE_WRAPPER_MAP;

/**
 * @author vv
 * @Description 鉴权
 * @date 2023/7/25-21:37
 */
@SPI("before")
public class ServerTokenFilterImpl implements IServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String token = String.valueOf(rpcInvocation.getAttachments().get("serviceToken"));
        ServiceWrapper serviceWrapper = PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
        String matchToken = String.valueOf(serviceWrapper.getServiceToken());
        if (CommonUtils.isEmpty(matchToken)) {
            return;
        }
        if (!CommonUtils.isEmpty(token) && token.equals(matchToken)) {
            return;
        }
        throw new RuntimeException("token is " + token + " , verify result is false!");
    }
}