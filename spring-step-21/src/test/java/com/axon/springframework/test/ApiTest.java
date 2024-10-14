package com.axon.springframework.test;

import com.alibaba.fastjson.JSON;
import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.test.dao.IAccountInfo;
import org.testng.annotations.Test;

import java.util.List;


public class ApiTest {


    /**
     *  总体的核心流程：
     *
     * 1.扫描注入MapperScannerConfigurer ,它又依赖sqlSessionFactory
     * 2.提前去修改Dao的BeanDefition 中的属性值，设置beanClass 为MapperFactoryBean
     * 3.  AccountInfo accountInfo = iAccountInfo.queryUserInfoById("黑狗"); 调用时，通过实际的代理对象去操作，则使用MapperFactoryBean
     *
     */
    @Test
    public void test_queryUserInfoById() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IAccountInfo iAccountInfo = classPathXmlApplicationContext.getBean("IAccountInfo", IAccountInfo.class);
        AccountInfo accountInfo = iAccountInfo.queryUserInfoById("黑狗"); // 这里是通过代理对象去操作数据库的
        System.out.println(JSON.toJSONString(accountInfo));

        List<AccountInfo> accountInfos = iAccountInfo.queryUserList();
        System.out.println(JSON.toJSONString(accountInfo));
    }
}
