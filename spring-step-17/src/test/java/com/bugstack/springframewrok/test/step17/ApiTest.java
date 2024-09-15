package com.bugstack.springframewrok.test.step17;

import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

public class ApiTest {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        System.out.println("测试结果：" + husband);
    }
}
