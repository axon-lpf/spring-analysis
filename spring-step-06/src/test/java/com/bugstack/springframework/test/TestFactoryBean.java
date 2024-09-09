package com.bugstack.springframework.test;

import cn.hutool.core.io.IoUtil;
import com.bugstack.springframework.bean.impl.UserServiceImpl;
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

    private DefaultResourceLoader  resourceLoader;

    @BeforeTest
    public  void  init() {
        resourceLoader = new DefaultResourceLoader();

    }

    @Test
    public  void  test_classpath() {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        try {
            InputStream inputStream = resource.getInputStream();
            String s = IoUtil.read(inputStream,"UTF-8");
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public  void   test_file(){
        Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
        try {
            InputStream inputStream = resource.getInputStream();
            String s = IoUtil.read(inputStream,"UTF-8");
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        //  将类的结构和信息注册到 BeanDefinition 中，
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        MyBeanFactoryPostProcessor myBeanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        myBeanFactoryPostProcessor.postProcessBeanFactory(factory);

        MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();

        factory.addBeanPostProcessor(myBeanPostProcessor);

        //获取bean的时候去创建
        UserServiceImpl userService = (UserServiceImpl) factory.getBean("userServiceImpl");

        userService.queryUserInfo();

        String s = userService.queryUserName("1");
        System.out.println(s);
        System.out.println(userService.getuId());
        System.out.println(userService.getName());
        System.out.println(userService.getAddress());


    }


    @Test
    public void test_xml() throws InstantiationException, IllegalAccessException {


        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        UserServiceImpl userService =(UserServiceImpl) classPathXmlApplicationContext.getBean("userServiceImpl");

        userService.queryUserInfo();

        String s = userService.queryUserName("1");
        System.out.println(s);
        System.out.println(userService.getuId());
        System.out.println(userService.getName());
        System.out.println(userService.getAddress());


    }
}
