package com.vv.core.router;

import com.vv.core.common.ChannelFutureWrapper;
import com.vv.core.registy.URL;

/**
 * @author vv
 * @Description 路由接口
 * @date 2023/7/24-13:46
 */
public interface IRouter {
    /**
     * 刷新路由数组
     *
     * @param selector
     */
    void refreshRouterArr(Selector selector);

    /**
     * 获取到请求到连接通道
     *
     * @return
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新权重信息
     *
     * @param url
     */
    void updateWeight(URL url);
}
