package com.vv.core.server;

import com.vv.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vv
 * @Description 测试数据
 * @date 2023/7/21-13:23
 */
public class DataServiceImpl implements DataService {
    @Override
    public String sendData(String body) {
        System.out.println("己收到的参数长度："+body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("idea1");
        arrayList.add("idea2");
        arrayList.add("idea3");
        return arrayList;
    }
    @Override
    public void testError() {
        System.out.println(1 / 0);
    }

    @Override
    public String testErrorV2() {
        throw new RuntimeException("测试异常");
//        return "three";
    }
}
