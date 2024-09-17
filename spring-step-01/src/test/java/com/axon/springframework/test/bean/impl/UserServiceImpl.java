package com.axon.springframework.test.bean.impl;

import com.axon.springframework.test.bean.IUserService;

public class UserServiceImpl implements IUserService {
    @Override
    public void queryUserInfo() {
        System.out.println("我是查询用户信息");
    }
}
