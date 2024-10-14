package com.axon.springframework.test;

import com.axon.springframework.BeanDefinition;
import com.axon.springframework.BeanFactory;
import com.axon.springframework.test.bean.IUserService;
import com.axon.springframework.test.bean.impl.UserServiceImpl;
import org.testng.annotations.Test;

/**
 *  本章主要添加简易的BeanDefinition 和 BeanFactory
 *  1. BeanDefinition  中存储了用户的实例对象
         public class BeanDefinition {
             private Object bean;

             public BeanDefinition(Object bean) {
                this.bean = bean;
             }
             public Object getBean() {
                return bean;
             }
         }
 *  2. BeanFactory 中存储了 BeanDefinition 的缓存
 *     核心代码：
 *          private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
 *          public Object getBean(String name) {
 *               return beanDefinitionMap.get(name).getBean();
 *          }
     *     public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
     *         beanDefinitionMap.put(name, beanDefinition);
     *     }
 *
 *   3. 使用如单元测试
 *
 */
public class TestFactory {


    @Test
    public void test_BeanFactory() {
        //初始化bean接口
        BeanFactory beanFactory = new BeanFactory();
        // 注册bean对象
        BeanDefinition beanDefinition = new BeanDefinition(new UserServiceImpl());
        beanFactory.registerBeanDefinition("userServiceImpl", beanDefinition);

        //获取bean对象
        IUserService userService = (IUserService) beanFactory.getBean("userServiceImpl");
        userService.queryUserInfo();

    }
}
