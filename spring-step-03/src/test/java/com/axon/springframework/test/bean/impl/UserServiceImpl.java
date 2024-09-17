package com.axon.springframework.test.bean.impl;

import com.axon.springframework.test.bean.IUserService;

public class UserServiceImpl implements IUserService {

    private String name;

    public UserServiceImpl() {
        System.out.println("我是无参构造函数");
    }

    public UserServiceImpl(String name) {
        System.out.println("我是有参构造函数");
        this.name = name;
    }

    @Override
    public void queryUserInfo() {
        System.out.println("我是查询用户信息" + this.name);
    }


    @Override
    public String toString() {
        return "UserServiceImpl{" +
                "name='" + name + '\'' +
                '}';
    }

}
