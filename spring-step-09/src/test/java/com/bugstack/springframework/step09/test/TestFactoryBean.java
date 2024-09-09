package com.bugstack.springframework.step09.test;

import com.bugstack.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.bugstack.springframework.core.io.DefaultResourceLoader;
import com.bugstack.springframework.step09.impl.UserServiceImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * 本章节主要是 factoryBean 的功能和作用
 * <p>
 * 本章节遇到的坑， 通过cglib代理后不能直接反射赋值，需要获取到父类的才能复制
 * <p>
 * //属性填充,一个巨坑。 由于 CGLIB 创建的是代理类，可以通过代理类的父类（即原始类）来获取字段。使用 getSuperclass() 方法获取原始类，并从中获取字段进行操作。
 * Class<?> aClass = bean.getClass().getSuperclass();
 * Field declaredField = aClass.getDeclaredField(name);
 * declaredField.setAccessible(true);
 * declaredField.set(bean, value);
 *
 * 以前的几个章节都有问题。
 */
public class TestFactoryBean {

    private DefaultResourceLoader resourceLoader;

    @BeforeTest
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }


    @Test
    public void test_factory_bean() throws InstantiationException, IllegalAccessException {


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
