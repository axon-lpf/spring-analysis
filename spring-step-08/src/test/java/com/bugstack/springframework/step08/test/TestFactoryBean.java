package com.bugstack.springframework.step08.test;

import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.bugstack.springframework.core.io.DefaultResourceLoader;
import com.bugstack.springframework.step08.impl.UserServiceImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * 本节功能的作用是用来感知对象。主要目的是用来扩展spring的功能，当开发srping中间件时，会经常用到这几个类
 * BeanNameAware, BeanClassLoadAware,BeanFactoryAware,ApplicationContextAware
 */
public class TestFactoryBean {

    private DefaultResourceLoader resourceLoader;

    @BeforeTest
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }


    @Test
    public void test_xml() throws InstantiationException, IllegalAccessException {


        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");


        classPathXmlApplicationContext.registerShutdownHook(); //注册一个钩子程序
        UserServiceImpl userService = (UserServiceImpl) classPathXmlApplicationContext.getBean("userServiceImpl");

        userService.queryUserInfo();

        String s = userService.queryUserName("1");
        System.out.println(s);
        System.out.println(userService.getuId());
        System.out.println(userService.getName());
        System.out.println(userService.getAddress());

        System.out.println("applicationContext" + userService.applicationContext);

        System.out.println("beanFactory" + userService.beanFactory);


    }
}
