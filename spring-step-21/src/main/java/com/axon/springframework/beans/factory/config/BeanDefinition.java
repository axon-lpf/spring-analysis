package com.axon.springframework.beans.factory.config;

import com.axon.springframework.beans.factory.PropertyValues;

public class BeanDefinition {

    private String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    private String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * 对象的类型
     */
    private Class<?> beanClass;

    /**
     * 对象的属性集合
     */
    private PropertyValues propertyValues;

    /**
     * 初始化方法名称
     */
    private String initMethodName;

    /**
     * 销毁方法名称
     */
    private String destroyMethodName;


    private boolean singleton = true;

    private boolean prototype = false;

    //默认单例模式
    private String scope = SCOPE_SINGLETON;


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


    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public  boolean  isSingleton(){
        return  this.scope.equals(SCOPE_SINGLETON);
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
