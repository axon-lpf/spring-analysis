package com.axon.springframework.step09.test;

import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.core.io.DefaultResourceLoader;
import com.axon.springframework.step09.impl.UserServiceImpl;
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
 *
 *
 * 本章主要是添加对FactoryBean的操作
 *  1.1>添加FactoryBeanRegistrySupport 类， 继承了DefaultSingletonBeanRegistry 类，  而 FactoryBeanRegistrySupport又AbstractBeanFactory 继承， 而
 *  AbstractBeanFactory又被和很类继承
 *  TODO 以下是 核心 方法
 *
 *      public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
 *
 *          //TODO 核心的FactoryBean缓存容器
     *     private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();
     *
 *          //TODO 从缓存中获取对应的FactoryBean
     *     protected Object getCachedObjectForFactoryBean(String beanName) {
     *
     *         Object object = this.factoryBeanObjectCache.get(beanName);
     *         return object != null ? object : null;
     *     }
     *
 *      //TODO 创建FactoryBean实列，并缓存到容器中
 *
     *     protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
     *         if (factoryBean.isSingleton()) {
     *             Object object = this.factoryBeanObjectCache.get(beanName);
     *             if (object == null) {
     *                 object = doGetObjectFromFactoryBean(factoryBean, beanName);
     *                 this.factoryBeanObjectCache.put(beanName, object);
     *                 return object != null ? object : null;
     *
     *             } else {
     *                 return doGetObjectFromFactoryBean(factoryBean, beanName);
     *             }
     *         }
     *         return null;
     *     }
     *
     *     private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, String beanName) {
     *         try {
     *             return factoryBean.getObject();
     *         } catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *         return null;
 *     }
 * }
 *
 *1.2> AbstractBeanFactory 中在获取doGetBean中时， 添加以下方法
 *          protected <T> T doGetBean(String beanName, Object... args) {
     *         Object sharedInstance = getSingleton(beanName);
     *         if (sharedInstance != null) {
 *              //TODO 核心方法 主要获取factoryBean
 *
     *             return (T) getObjectForBeanInstance(sharedInstance, beanName);
     *         }
     *         BeanDefinition beanDefinition = getBeanDefinition(beanName);
     *         Object bean = createBean(beanName, beanDefinition, args);
 *            //TODO 核心方法 主要获取factoryBean  一级缓存中没有拿到， 则走这一步。
     *         return (T) getObjectForBeanInstance(bean, beanName);
 *     }
 *
 *     private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
 *         if (!(beanInstance instanceof FactoryBean)) {
 *             return beanInstance;
 *         }
 *         Object object = getCachedObjectForFactoryBean(beanName);
 *         if (object == null) {
 *             FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
 *             object = getObjectFromFactoryBean(factoryBean, beanName);
 *
 *         }
 *         return object;
 *     }
 *
 *         private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, String beanName) {
     *         try {
     *             return factoryBean.getObject();
     *         } catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *         return null;
 *     }
 *
 * 1.3> 实际中使用情况
 *
 *      public class ProxyFactoryBean implements FactoryBean<IUserDao> {
 *
 *              //TODO 核心方法 ， 对应的 是 doGetObjectFromFactoryBean 方法中获取的内容， 获取的是一个代理对象
         *     @Override
         *     public IUserDao getObject() throws Exception {
         *
         *         InvocationHandler invocationHandler = (proxy, method, args) -> {
         *             Map<String, String> map = new HashMap<>();
         *             map.put("1", "收款单上岛咖啡");
         *             map.put("2", "我虞城县");
         *             map.put("3", "阿毛");
         *
         *             return "你被代理了" + method.getName() + ":" + map.get(args[0].toString());
         *         };
         *         return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserDao.class}, invocationHandler);
         *     }
         *
         *     @Override
         *     public Class<?> getObjectType() {
         *         return IUserDao.class;
         *     }
         *
         *     @Override
         *     public boolean isSingleton() {
         *         return true;
         *     }
 * }
 *
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
