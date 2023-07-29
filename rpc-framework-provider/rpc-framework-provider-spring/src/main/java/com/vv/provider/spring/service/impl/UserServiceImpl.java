package com.vv.provider.spring.service.impl;

import com.vv.framework.spring.starter.annotation.VRpcService;
import com.vv.interfaces.UserService;



@VRpcService
public class UserServiceImpl implements UserService {

    public void test() {
        System.out.println("test");
    }
}
