package com.bugstack.springframework.beans.factory.config;

import com.bugstack.springframework.beans.factory.PropertyValues;

public class BeanDefinition {

    /**
     * 对象的类型
     */
    private Class<?> beanClass;

    /**
     * 对象的属性集合
     */
    private PropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();

    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }


    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

}
