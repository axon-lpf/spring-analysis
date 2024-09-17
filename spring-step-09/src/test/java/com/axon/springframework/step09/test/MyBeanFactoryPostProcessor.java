package com.axon.springframework.step09.test;

import com.axon.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.axon.springframework.beans.factory.PropertyValue;
import com.axon.springframework.beans.factory.PropertyValues;
import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        System.out.println("我是实例化前处理");
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userServiceImpl");

        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("name", "我是一颗小小鸟"));

    }
}
