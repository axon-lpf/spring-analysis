package com.axon.springframework.test;

import cn.hutool.core.io.IoUtil;
import com.axon.springframework.bean.impl.UserServiceImpl;
import com.axon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.axon.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.axon.springframework.core.io.DefaultResourceLoader;
import com.axon.springframework.core.io.Resource;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;


/**
 *  本章节主要是添加读取xml文件，去解析类的对象的属性和值，并注册到BeanDefinition中， 在去创建bean对象。
 *  核心代码块：
 *      1.1>创建  XmlBeanDefinitionReader 读取配置类
 *      1.2> XmlBeanDefinitionReader 中添加构造函数初始化
 *          public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
 *              //TODO 核心传入对应BeanDefinitionRegistry 的实现， 即DefaultListableBeanFactory 实现了  BeanDefinitionRegistry
         *     public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
         *         super(registry);
         *     }
 *
         *     public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
         *         super(registry);
         *     }
 *     }
 *      1.3> 加载配置文件
 *            @Override
         *     public void loadBeanDefinitions(String location) {
         *         ResourceLoader resourceLoader = getResourceLoader();
 *                 //TODO 加载配置文件
         *         Resource resource = resourceLoader.getResource(location);
 *                 //TODO 解析
         *         loadBeanDefinitions(resource);
         *
         *     }
 *      1.4> 解析xml中的属性， 注册BeanDefinition，即添加到缓存中去
 *           protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
     *         Document document = XmlUtil.readXML(inputStream);
     *         Element root = document.getDocumentElement();
     *         NodeList childNodes = root.getChildNodes();
     *         for (int i = 0; i < childNodes.getLength(); i++) {
     *
     *             if (!(childNodes.item(i) instanceof Element)) {
     *                 continue;
     *             }
     *             if (!"bean".equals(childNodes.item(i).getNodeName())) {
     *                 continue;
     *             }
     *             //开始解析标签
     *             Element bean = (Element) childNodes.item(i);
     *             String id = bean.getAttribute("id");
     *             String name = bean.getAttribute("name");
     *             String aClass = bean.getAttribute("class");
     *
     *             Class<?> clazz = Class.forName(aClass);
     *             //优先级id >name
     *             String beanName = StrUtil.isNotEmpty(id) ? id : name;
     *             if (StrUtil.isEmpty(beanName)) {
     *                 beanName = StrUtil.lowerFirst(clazz.getSimpleName());
     *             }
     *             BeanDefinition beanDefinition = new BeanDefinition(clazz);
     *
     *             for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
     *
     *                 if (!(bean.getChildNodes().item(j) instanceof Element)) {
     *                     continue;
     *                 }
     *                 if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) {
     *                     continue;
     *                 }
     *                 Element property = (Element) bean.getChildNodes().item(j);
     *
     *                 String attrName = property.getAttribute("name");
     *                 String attrValue = property.getAttribute("value");
     *                 String attrRef = property.getAttribute("ref");
     *
     *                 //TODO  获取属性值
     *                 Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
     *
     *                 //TODO 创建属性属性
     *                 PropertyValue propertyValue = new PropertyValue(attrName, value);
     *                 beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
     *             }
     *             if (getRegistry().containsBeanDefinition(beanName)) {
     *
     *                 throw new RuntimeException("注册BeanDefinition 异常");
     *             }
     *             //TODO 注册beanBeanDefinition
     *             getRegistry().registerBeanDefinition(beanName, beanDefinition);
     *
     *         }
 *       }
 *
 *    1.5> 使用，如单元测试的使用
 *          @Test
     *     public void test() {
     *
     *         DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
     *         // TODO  将类的结构和信息注册到 BeanDefinition 中，
     *         XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
 *              //TODO  加载
     *         xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
     *
     *         //TODO 获取bean的时候去创建, 从BeanDefinition缓存中获取
     *         UserServiceImpl userService = (UserServiceImpl) factory.getBean("userServiceImpl");
     *
     *         userService.queryUserInfo();
     *
     *         String s = userService.queryUserName("1");
     *         System.out.println(s);
     *         System.out.println(userService.getuId());
     *         System.out.println(userService.getName());
     *
     *     }
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

        //获取bean的时候去创建
        UserServiceImpl userService = (UserServiceImpl) factory.getBean("userServiceImpl");

        userService.queryUserInfo();

        String s = userService.queryUserName("1");
        System.out.println(s);
        System.out.println(userService.getuId());
        System.out.println(userService.getName());

    }
}
