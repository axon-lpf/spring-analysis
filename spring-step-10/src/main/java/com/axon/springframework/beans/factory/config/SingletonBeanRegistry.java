package com.axon.springframework.beans.factory.config;

public interface SingletonBeanRegistry {

    Object getSingleton(String name);


    void registerSingletonBean(String beanName, Object singletonBean);
}
