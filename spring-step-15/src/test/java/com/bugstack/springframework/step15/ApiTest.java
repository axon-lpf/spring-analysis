package com.bugstack.springframework.step15;

import com.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.bugstack.springframework.test.bean.IUserService;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class ApiTest {


    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }
}
