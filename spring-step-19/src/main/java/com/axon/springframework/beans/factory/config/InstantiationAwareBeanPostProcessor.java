package com.axon.springframework.beans.factory.config;

import com.axon.springframework.beans.factory.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     *  创建代理
     * @param beanClass
     * @param beanName
     * @return
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);


    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);



    default Object  getEarlyBeanReference(Object bean, String beanName){return bean;}
}
