package com.axon.springframework.test;

import com.axon.springframework.bean.UserDao;
import com.axon.springframework.bean.impl.UserServiceImpl;
import com.axon.springframework.beans.factory.PropertyValue;
import com.axon.springframework.beans.factory.PropertyValues;
import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.BeanReference;
import com.axon.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.testng.annotations.Test;

/**
 *  本章节主要是添加了对beanDefinition中的属性设置， 即属性名称、属性值
 *  核心代码块：
 *      1.1> 添加PropertyValues 用于存储未创建bean对象之前的属性和值， 方便后续处理bean对象赋值使用
 *          public class PropertyValues {
         *     private final List<PropertyValue> propertyValues = new ArrayList<>();
         *
 *             //TODO 核心代码，添加属性名称和值
         *     public void addPropertyValue(PropertyValue value) {
         *         this.propertyValues.add(value);
         *     }
 *     }
 *     1.2> 如果是引用类型，则使用了BeanReference 去包装
 *          public class BeanReference {
         *     private  final String beanName;
         *     public BeanReference(String beanName) {
         *         this.beanName=beanName;
         *     }
         *
         *     public String getBeanName() {
         *         return beanName;
         *     }
        }

      1.3> beanDefinition 中加入对 PropertyValues 的依赖
             private PropertyValues propertyValues;

             public BeanDefinition(Class<?> beanClass) {
             this.beanClass = beanClass;
             this.propertyValues = new PropertyValues();

             }

             public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
             this.beanClass = beanClass;
             this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
             }

      1.4> AbstractAutowireCapableBeanFactory 类中， 创建bean对象时添加了属性赋值的逻辑
         @Override
         protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
             // TODO 创建bean实例
             Object bean = createBeanInstance(beanName, beanDefinition, args);
             // TODO 设置属性赋值
             applyPropertyValues(beanName, bean, beanDefinition);
             registerSingletonBean(beanName, bean);
             return bean;
         }

      1.5> AbstractAutowireCapableBeanFactory 类中 ，设置属性赋值的具体方法
         @Override
         protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
             try {
                 PropertyValues propertyValues = beanDefinition.getPropertyValues();
                 PropertyValue[] valuesPropertyValues = propertyValues.getPropertyValues();
                 for (PropertyValue propertyValue : valuesPropertyValues) {
                 String name = propertyValue.getName();
                 Object value = propertyValue.getValue();
                 if (value instanceof BeanReference) {
                    //TODO 如果是引用类型，则继续获取bean,调用属性赋值
                     BeanReference beanReference = (BeanReference) value;
                     //TODO 调用getBean 时，有会进入applyPropertyValues 方法，属于递归操作。
                     value = getBean(beanReference.getBeanName());
                 }
                 //TODO 属性填充赋值
                 Class<?> aClass = bean.getClass();
                 Field declaredField = aClass.getDeclaredField(name);
                 declaredField.setAccessible(true);
                 declaredField.set(bean, value);
                 }
             } catch (NoSuchFieldException e) {
             e.printStackTrace();
             } catch (IllegalAccessException e) {
             e.printStackTrace();
             }
         }
     1.6>调用方式， 如以下的单元测试的调用
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

 *
 *
 *
 */
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
