package com.bugstack.springframework.test;

import com.bugstack.springframework.BeanDefinition;
import com.bugstack.springframework.BeanFactory;
import com.bugstack.springframework.test.bean.IUserService;
import com.bugstack.springframework.test.bean.impl.UserServiceImpl;
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
