package com.bugstack.springframework.step09.test;

import com.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import com.bugstack.springframework.step09.impl.UserServiceImpl;

public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if ("userServiceImpl".equals(beanName)) {
            System.out.println("我是初始化前操作");
            UserServiceImpl userService = (UserServiceImpl) bean;
            userService.setAddress("北京天上人间");
            return userService;

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if ("userServiceImpl".equals(beanName)) {
            System.out.println("我是初始后操作");
        }
        return bean;
    }
}
