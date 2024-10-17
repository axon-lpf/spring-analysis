package com.axon.springframework.step08.test;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.core.io.DefaultResourceLoader;
import com.axon.springframework.step08.impl.UserServiceImpl;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * 在 Spring 中，Aware 是一组回调接口，用于让 Bean 获得对 Spring 容器基础设施组件的访问。这些接口的实现类能够通过依赖注入的方式获取到特定的 Spring 容器资源，如 ApplicationContext、BeanFactory 等。
 *
 * Aware 接口本质上是 Spring 提供的一种机制，让某些 Bean 能够通过实现特定的接口获得 Spring 容器的内部资源，而无需显式地依赖 Spring 容器的 API。
 *
 * 常见的 Aware 接口
 * ApplicationContextAware: 让 Bean 获取 ApplicationContext 对象。
 * BeanFactoryAware: 让 Bean 获取 BeanFactory 对象。
 * BeanNameAware: 让 Bean 获取当前 Bean 的名称。
 * EnvironmentAware: 让 Bean 获取 Environment 对象，通常用于访问环境变量、属性配置等。
 * ResourceLoaderAware: 让 Bean 获取 ResourceLoader，用于加载文件资源。
 *
 * Spring 的 Aware 接口非常有用，能够让 Bean 获取到 Spring 容器的内部资源，提升了 Bean 的灵活性。
 * 在实际开发中，最常见的接口是 ApplicationContextAware 和 BeanFactoryAware，它们可以帮助开发者动态获取其他 Bean 或者访问容器级别的资源。
 *
 *
 * 本节功能的作用是用来感知对象。主要目的是用来扩展spring的功能，当开发srping中间件时，会经常用到这几个类
 * BeanNameAware, BeanClassLoadAware,BeanFactoryAware,ApplicationContextAware
 * 核心代码块：
 *  1.1> 添加对应的aware接口
 *  1.2> BeanNameAware, BeanClassLoadAware,BeanFactoryAware  分别继承这个AWare这个接口
 *  1.3>对应的实现类实现对 以上的接口继承
 *      public class UserServiceImpl implements IUserService, InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, ApplicationContextAware {
 *          //TODO 实现对应的方法
 *      }
 *  1.4.AbstractAutowireCapableBeanFactory 类中在实列化放是添加对Aware接口的判断处理。
 *
 *              @Override
 *       protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
 *         // 创建bean实例
 *         Object bean = createBeanInstance(beanName, beanDefinition, args);
 *         // 设置属性填充
 *         applyPropertyValues(beanName, bean, beanDefinition);
 *
 *         //执行初始化方法 ,初始化前， 初始化后
 *         bean = initializeBean(beanName, bean, beanDefinition);
 *
 *         //注册实现DisposableBean 的接口
 *         registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
 *         registerSingletonBean(beanName, bean);
 *         return bean;
 *     }
 *
 *          private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
 *
 *         //TODO 设置感知对象 核心代码块
 *         if (bean instanceof Aware) {
 *             if (bean instanceof BeanFactoryAware) {
 *                 ((BeanFactoryAware) bean).setBeanFactory(this);
 *             }
 *             if (bean instanceof BeanClassLoaderAware) {
 *                 ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
 *             }
 *             if (bean instanceof BeanNameAware) {
 *                 ((BeanNameAware) bean).setBeanName(beanName);
 *             }
 *         }
 *         // 初始化前处理
 *         Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
 *         // 执行初始化方法
 *         invokeInitMethods(beanName, wrappedBean, beanDefinition);
 *         // 初始化后处理
 *         wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *         return wrappedBean;
 *
 *     }
 *
 *
 *
 */
public class TestFactoryBean {

    private DefaultResourceLoader resourceLoader;

    @BeforeTest
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }


    @Test
    public void test_xml() throws InstantiationException, IllegalAccessException {


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
