package com.axon.springframework.test;

import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.axon.springframework.test.bean.IUserService;
import com.axon.springframework.test.bean.impl.UserServiceImpl;
import org.testng.annotations.Test;

public class TestFactory {


    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

        defaultListableBeanFactory.registerBeanDefinition("userService", new BeanDefinition(UserServiceImpl.class));

        IUserService userService = (IUserService) defaultListableBeanFactory.getBean("userService");

        userService.queryUserInfo();

        // 再次获取调用
        userService = (IUserService) defaultListableBeanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
