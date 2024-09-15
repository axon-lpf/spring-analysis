package com.bugstack.springframework.test;

import com.alibaba.fastjson.JSON;
import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.bugstack.springframework.test.dao.IAccountInfo;
import org.testng.annotations.Test;


public class ApiTest {


    @Test
    public void test_queryUserInfoById() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IAccountInfo iAccountInfo = classPathXmlApplicationContext.getBean("IAccountInfo", IAccountInfo.class);
        AccountInfo accountInfo = iAccountInfo.queryUserInfoById("黑狗");
        System.out.println(JSON.toJSONString(accountInfo));
    }
}
