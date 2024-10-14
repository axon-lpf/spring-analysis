package com.axon.springframework.test;

import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.axon.springframework.test.bean.IUserService;
import com.axon.springframework.test.bean.impl.UserServiceImpl;
import org.testng.annotations.Test;

/**
 *  本章节在第一章节的基础上进行扩展
 *  核心步骤：
 *  1.1> 添加 SingletonBeanRegistry 注册单列bean的接口
 *       添加 BeanDefinitionRegistry  注册BeanDefinition的接口
 *       添加 BeanFactory  是获取单列bean的接口
 *  1.2>DefaultSingletonBeanRegistry  实现 SingletonBeanRegistry 的接口
 *      AbstractBeanFactory抽象类 又继承 了DefaultSingletonBeanRegistry， 并实现了 BeanFactory的方法
 *
 *      AbstractAutowireCapableBeanFactory 又继承了AbstractBeanFactory,并重新了createBean
 *
 *      DefaultListableBeanFactory 又继承了 AbstractAutowireCapableBeanFactory，并实现了BeanDefinitionRegistry 接口
 *
 *  1.3>使用如单元测试中的方法
 *         DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
 *         defaultListableBeanFactory.registerBeanDefinition("userService", new BeanDefinition(UserServiceImpl.class));
 *         IUserService userService = (IUserService) defaultListableBeanFactory.getBean("userService");
 *
 *
 *
 */
public class TestFactory {


    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

        defaultListableBeanFactory.registerBeanDefinition("userService", new BeanDefinition(UserServiceImpl.class));

        IUserService userService = (IUserService) defaultListableBeanFactory.getBean("userService");

        userService.queryUserInfo();

        // 再次获取调用
        userService = (IUserService) defaultListableBeanFactory.getBean("userService");
        userService.queryUserInfo();
    }
}
