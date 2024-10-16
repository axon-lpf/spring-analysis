package com.axon.springframework.test;

import com.axon.springframework.bean.step7.impl.UserServiceImpl;
import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.core.io.DefaultResourceLoader;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * 本章主要添加了bean的销毁设计 及destroy
 * 核心代码块；
 * 1.1> 添加处理销毁方法接口 DisposableBean
 *      //TODO 核心步骤1
 *      public interface DisposableBean {
 *          void  destroy() throws Exception;
 *      }
 *
 * 1.2> 在DefaultSingletonBeanRegistry 中添加 Disposable 缓存， 并添加。
 *      //TODO 核心步骤2
 *      private final Map<String, DisposableBean> disposableBeans = new HashMap<>();
 *
         public void registerDisposableBean(String beanName,DisposableBean bean){
                *disposableBeans.put(beanName,bean);
 *      }
 *
 *      添加批量销毁的方法
 *        public void destroySingletons() {
 *         Set<String> keySet = this.disposableBeans.keySet();
 *         Object[] disposableBeanNames = keySet.toArray();
 *
 *         for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
 *             Object beanName = disposableBeanNames[i];
 *             DisposableBean disposableBean = disposableBeans.remove(beanName);
 *             try {
 *                 disposableBean.destroy();
 *             } catch (Exception e) {
 *                 throw new RuntimeException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
 *             }
 *         }
 *     }
 *
 *1.3> AbstractApplicationContext 中添加了close 方法，  close 方法调用 在DefaultSingletonBeanRegistry  中的destroySingletons 方法。
 *
 *1.4> AbstractAutowireCapableBeanFactory 中的添加注册   DisposeableBean方法
 *          @Override
 *     protected Object createBean(String beanName, BeanDefinition beanDefinition) {
 *
 *         Object bean = null;
 *         try {
 *             bean = beanDefinition.getBeanClass().newInstance();
 *         } catch (InstantiationException e) {
 *             e.printStackTrace();
 *         } catch (IllegalAccessException e) {
 *             e.printStackTrace();
 *         }
 *         //注册bean的属性
 *         applyPropertyValues(beanName, bean, beanDefinition);
 *
 *         //执行初始化方法 ,初始化前， 初始化后
 *         bean = initializeBean(beanName, bean, beanDefinition);
 *
 *         //TODO 核心步骤 注册实现DisposableBean 的接口
 *         registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
 *
 *         //注册bean
 *         registerSingletonBean(beanName, bean);
 *         return bean;
 *     }
 *
 *1.5 AbstractApplicationContex中添加以下方法，
 *          @Override
 *     public void registerShutdownHook() {
 *          //TODO 注册钩子程序， 调用close 方法， 销毁bean 。
 *         Runtime.getRuntime().addShutdownHook(new Thread(this::close));
 *     }
 *     ///TODO  被registerShutdownHook 的调用
 *     @Override
 *     public void close() {
 *         //销毁单列对象
 *         getBeanFactory().destroySingletons();
 *     }
 *
 * 1.6 实际使用方式， 如以下的单元测试 。
 *         ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
 *         //TODO 注册一个钩子程序， 应用程序关闭时触发。
 *         classPathXmlApplicationContext.registerShutdownHook();
 *         UserServiceImpl userService = (UserServiceImpl) classPathXmlApplicationContext.getBean("userServiceImpl");
 *
 *         userService.queryUserInfo();
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


    }
}
