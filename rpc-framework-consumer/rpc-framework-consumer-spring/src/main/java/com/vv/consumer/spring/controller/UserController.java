package com.vv.consumer.spring.controller;


import com.vv.framework.spring.starter.annotation.VRpcReference;
import com.vv.interfaces.OrderService;
import com.vv.interfaces.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping(value = "/user")
public class UserController {

    @VRpcReference
    private UserService userService;

    /**
     * 验证各类参数配置是否异常
     */
    @VRpcReference(group = "order-group",serviceToken = "order-token")
    private OrderService orderService;

    @GetMapping(value = "/test")
    public void test(){
        userService.test();
    }


    @GetMapping(value = "/testMaxData")
    public String testMaxData(int i){
        String result = orderService.testMaxData(i);
        System.out.println(result.length());
        return result;
    }


    @GetMapping(value = "/get-order-no")
    public List<String> getOrderNo(){
        List<String> result =  orderService.getOrderNoList();
        System.out.println(result);
        return result;
    }

}
