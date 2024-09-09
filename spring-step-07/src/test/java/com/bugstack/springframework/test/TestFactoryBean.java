package com.bugstack.springframework.test;

import cn.hutool.core.io.IoUtil;
import com.bugstack.springframework.bean.step7.impl.UserServiceImpl;
import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.bugstack.springframework.core.io.DefaultResourceLoader;
import com.bugstack.springframework.core.io.Resource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

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


    }
}
