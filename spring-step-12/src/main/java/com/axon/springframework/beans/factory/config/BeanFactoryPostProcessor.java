package com.axon.springframework.beans.factory.config;

import com.axon.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * 用于所有BeanDefinition加载完成之后，且将Bean对象实例化之前，提供修改BeanDefinition属性的机制
 */
public interface BeanFactoryPostProcessor {

    /**
     * 用于所有BeanDefinition加载完成之后，且将Bean对象实例化之前，提供修改BeanDefinition属性的机制
     *
     * @param beanFactory
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
