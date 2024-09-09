package com.bugstack.springframework.test;

import com.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugstack.springframework.beans.factory.PropertyValue;
import com.bugstack.springframework.beans.factory.PropertyValues;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        System.out.println("我是实例化前处理");
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userServiceImpl");

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("name", "我是一颗小小鸟"));

    }
}
