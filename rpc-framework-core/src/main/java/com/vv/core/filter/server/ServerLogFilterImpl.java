package com.vv.core.filter.server;

import com.vv.core.common.RpcInvocation;
import com.vv.core.common.annotation.SPI;
import com.vv.core.filter.IServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vv
 * @Description 服务端日志过滤器
 * @date 2023/7/25-21:56
 */
@SPI("before")
public class ServerLogFilterImpl implements IServerFilter {

    private static Logger logger = LoggerFactory.getLogger(ServerLogFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        logger.debug(rpcInvocation.getAttachments().get("c_app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }

}