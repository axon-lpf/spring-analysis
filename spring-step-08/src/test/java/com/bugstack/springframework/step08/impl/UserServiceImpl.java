package com.bugstack.springframework.step08.impl;

import com.bugstack.springframework.beans.factory.*;
import com.bugstack.springframework.beans.factory.context.ApplicationContext;
import com.bugstack.springframework.beans.factory.context.ApplicationContextAware;
import com.bugstack.springframework.step08.IUserService;
import com.bugstack.springframework.step08.UserDao;

public class UserServiceImpl implements IUserService, InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, ApplicationContextAware {

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


    public ApplicationContext applicationContext;

    public BeanFactory beanFactory;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("classLoader" + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("bean name is :" + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
