package com.bugstack.springframework.test;

import com.bugstack.springframework.bean.IUserService;
import com.bugstack.springframework.bean.UserDao;
import com.bugstack.springframework.bean.impl.UserServiceImpl;
import com.bugstack.springframework.beans.factory.PropertyValue;
import com.bugstack.springframework.beans.factory.PropertyValues;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.config.BeanReference;
import com.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.testng.annotations.Test;

public class TestFactoryBean {

    @Test
    public void test() {

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        //需要注入的属性，放入到BeanDefinition
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "0001"));
        propertyValues.addPropertyValue(new PropertyValue("name", "黄飞鸿"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
        factory.registerBeanDefinition("userService", new BeanDefinition(UserServiceImpl.class, propertyValues));
        //获取bean
        UserServiceImpl userService = (UserServiceImpl) factory.getBean("userService");

        userService.queryUserInfo();

        String s = userService.queryUserName("1");
        System.out.println(s);
        System.out.println(userService.getuId());
        System.out.println(userService.getName());

    }
}
