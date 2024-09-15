package com.bugstack.springframework.step18;

import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.bugstack.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class ApiTest {


    private JdbcTemplate jdbcTemplate;

    @BeforeTest
    public void before() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        jdbcTemplate = classPathXmlApplicationContext.getBean("jdbcTemplate", JdbcTemplate.class);
    }


    @Test
    public void queryForListTest() {
        List<Map<String, Object>> allResult = jdbcTemplate.queryForList("select * from account");
        for (Map<String, Object> objectMap : allResult) {
            System.out.println("测试结果：" + objectMap);
        }
    }




}
