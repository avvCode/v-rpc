package com.vv.provider.spring.service.impl;

import com.vv.framework.spring.starter.annotation.VRpcService;
import com.vv.interfaces.OrderService;


import java.util.Arrays;
import java.util.List;


@VRpcService(serviceToken = "order-token",group = "order-group",limit = 2)
public class OrderServiceImpl implements OrderService {

    @Override
    public List<String> getOrderNoList() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList("item1","item2");
    }

    //测试大数据包传输是否有异常
    @Override
    public String testMaxData(int i) {
        StringBuffer stb = new StringBuffer();
        for(int j=0;j<i;j++){
            stb.append("1");
        }
        return stb.toString();
    }
}
