package com.axon.springframework.test;

import cn.hutool.core.io.IoUtil;
import com.axon.springframework.bean.impl.UserServiceImpl;
import com.axon.springframework.beans.factory.context.support.ClassPathXmlApplicationContext;
import com.axon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.axon.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.axon.springframework.core.io.DefaultResourceLoader;
import com.axon.springframework.core.io.Resource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 *  本章节主要添加了 BeanFactoryPostProcessor  和 BeanPostProcessor的使用
 *  BeanFactoryPostProcessor 作用：
 *      BeanFactoryPostProcessor 允许我们在 BeanFactory 标准初始化之后、Bean实例化之前 对 Spring 容器中的 bean 定义（BeanDefinition）进行修改。其主要作用是允许开发者在实际创建 bean 对象之前调整 bean 的定义，修改其属性、范围、依赖注入等配置。
         * 核心特点：
         *
         * 	•	运行时机：在容器完成 BeanFactory 的标准初始化后（即所有的 BeanDefinition 已经加载完毕，但是还没有实例化任何 bean 之前），执行 BeanFactoryPostProcessor。
         * 	•	处理对象：BeanFactoryPostProcessor 处理的是 BeanDefinition，而不是实际的 bean 实例。
         * 	•	常见用途：
         * 	•	修改 bean 的定义，比如修改 bean 的 scope（作用域），或添加/删除某些属性。
         * 	•	替换 PropertySources（属性源）中的配置，常见的例子是通过 PropertyPlaceholderConfigurer 修改占位符的值。
 *  BeanPostProcessor作用：
 *      BeanPostProcessor 是用来对 已创建的 bean 实例 进行自定义修改的，它会在 Spring 容器实例化 bean 后对其进行操作，通常用于在 bean 初始化（@PostConstruct 或 afterPropertiesSet() 方法之前或之后）时对其进行增强或代理。
         * 核心特点：
         *
         * 	•	运行时机：BeanPostProcessor 的两个方法 postProcessBeforeInitialization 和 postProcessAfterInitialization 会在 bean 实例化之后、初始化方法调用之前和之后执行。
         * 	•	处理对象：它处理的是 实际的 bean 实例，而不是 BeanDefinition。
         * 	•	常见用途：
         * 	•	用于在 bean 初始化时添加一些自定义逻辑，比如 AOP 代理的创建。
         * 	•	在 bean 初始化后，添加额外的增强行为，比如对 bean 进行代理增强（如创建动态代理、性能监控、事务管理等）。
 *
 * 	 核心代码块：
 * 	    1.1> 添加 ClassPathXmlApplicationContext 类
 *
         public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
                 private String[] configLocations;

                 public ClassPathXmlApplicationContext() {

                 }

                 public ClassPathXmlApplicationContext(String configLocations) {
                    //TODO 加载配置
                    this(new String[]{configLocations});
                 }

                 public ClassPathXmlApplicationContext(String[] configLocations) {
                     this.configLocations = configLocations;
                    //TODO 核心代码， 刷新上下文
                     refresh();
                 }

                 @Override
                 protected String[] getConfigLocations() {
                    return configLocations;
                 }
         }

        1.2> 核心的refresh()
             @Override
             public void refresh() {
                 //TODO 1.创建beanFactory,并加载BeanDefinition
                 refreshBeanFactory();

                 //TODO 2.获取beanFactory,获取已经构建好 BeanDefinition 的缓存
                 ConfigurableListableBeanFactory beanFactory = getBeanFactory();

                 //TODO 3.在bean对象实例化之前，执行BeanFactoryPostProcessor操作
                 invokeBeanFactoryPostProcessor(beanFactory);

                 //TODO 4.BeanPostProcessor需要子啊将bean对象实例化之前注册
                 registerBeanPostProcessor(beanFactory);

                 //TODO 5.提前实例化单例bean对象
                 beanFactory.preInstantiateSingletons();

             }

        1.3>在AbstraractApplicationContext中添加核心方法，供refresh()调用
            // 对应的第三步骤，调用处理  BeanFactoryPostProcessor 的相关对象
             private void invokeBeanFactoryPostProcessor(ConfigurableListableBeanFactory beanFactory) {
                //TODO Hex  获取对应的实例
                 Map<String, BeanFactoryPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
                 for (BeanFactoryPostProcessor beanFactoryPostProcessor : beansOfType.values()) {
                    //循环调用处理
                    beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
                 }
             }

             //对应的第四步，提前注册 BeanPostProcessor
             private void registerBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
                // TODO 核心获取
                 Map<String, BeanPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanPostProcessor.class);
                 for (BeanPostProcessor beanPostProcessor : beansOfType.values()) {
                 beanFactory.addBeanPostProcessor(beanPostProcessor);
             }

        1.4> 对应DefaultListableBeanFactory
             @Override
             public <T> Map<String, T> getBeansOfType(Class<T> type) {
                 Map<String, T> result = new HashMap<>();
                 beanDefinitionMap.forEach((beanName, beanDefinition) -> {
                 Class beanClass = beanDefinition.getBeanClass();
                    //TODO 核心判断
                     if (type.isAssignableFrom(beanClass)) {
                     result.put(beanName, (T) getBean(beanName));
                 }
                 });
                 return result;
             }

        1.5>自定义BeanFactoryPostProcessor  和  BeanPostProcessor
             public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
                     @Override
                     public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

                     System.out.println("我是实例化前处理");
                     BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userServiceImpl");

                     PropertyValues propertyValues = beanDefinition.getPropertyValues();

                     propertyValues.addPropertyValue(new PropertyValue("name", "我是一颗小小鸟"));
                     }
             }

             public class MyBeanPostProcessor implements BeanPostProcessor {

             @Override
             public Object postProcessBeforeInitialization(Object bean, String beanName) {
                 if ("userServiceImpl".equals(beanName)) {
                     System.out.println("我是初始化前操作");
                     UserServiceImpl userService = (UserServiceImpl) bean;
                     userService.setAddress("北京天上人间");
                     return userService;

                 }
                 return bean;
             }

             @Override
             public Object postProcessAfterInitialization(Object bean, String beanName) if ("userServiceImpl".equals(beanName)) {
                         System.out.println("我是初始后操作");
                     }
                     return bean;
                 }
             }

        1.6>自定义调用测试

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


 *
 *
 *
 *
 */
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
