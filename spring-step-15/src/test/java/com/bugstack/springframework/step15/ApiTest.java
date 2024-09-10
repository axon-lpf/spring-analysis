package com.bugstack.springframework.step15;

import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;


public class ApiTest {


    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }
}
