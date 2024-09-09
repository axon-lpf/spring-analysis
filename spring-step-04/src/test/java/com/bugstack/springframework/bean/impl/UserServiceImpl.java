package com.bugstack.springframework.bean.impl;

import com.bugstack.springframework.bean.IUserService;
import com.bugstack.springframework.bean.UserDao;

public class UserServiceImpl implements IUserService {

    private String name;

    private String uId;

    public String getName() {
        return name;
    }

    public String getuId() {
        return uId;
    }

    private UserDao userDao;

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
    public String queryUserName(String userId) {
        return userDao.queryUser(userId);
    }


    @Override
    public String toString() {
        return "UserServiceImpl{" +
                "name='" + name + '\'' +
                '}';
    }

}
