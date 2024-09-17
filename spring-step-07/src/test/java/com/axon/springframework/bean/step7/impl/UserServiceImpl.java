package com.axon.springframework.bean.step7.impl;

import com.axon.springframework.bean.step7.IUserService;
import com.axon.springframework.bean.step7.UserDao;
import com.axon.springframework.beans.factory.DisposableBean;
import com.axon.springframework.beans.factory.InitializingBean;

public class UserServiceImpl implements IUserService, InitializingBean, DisposableBean {

    private String name;

    private String uId;


    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    @Override
    public void destroy() throws Exception {
        System.out.println("执行user-service destroy");

    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("执行user-service 中afterPropertiesSet");

    }
}
