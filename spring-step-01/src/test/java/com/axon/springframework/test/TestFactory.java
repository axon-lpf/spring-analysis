package com.axon.springframework.test;

import com.axon.springframework.BeanDefinition;
import com.axon.springframework.BeanFactory;
import com.axon.springframework.test.bean.IUserService;
import com.axon.springframework.test.bean.impl.UserServiceImpl;
import org.testng.annotations.Test;

public class TestFactory {


    @Test
    public void test_BeanFactory() {
        //初始化bean接口
        BeanFactory beanFactory = new BeanFactory();
        // 注册bean对象
        BeanDefinition beanDefinition = new BeanDefinition(new UserServiceImpl());
        beanFactory.registerBeanDefinition("userServiceImpl", beanDefinition);

        //获取bean对象
        IUserService userService = (IUserService) beanFactory.getBean("userServiceImpl");
        userService.queryUserInfo();

    }
}
