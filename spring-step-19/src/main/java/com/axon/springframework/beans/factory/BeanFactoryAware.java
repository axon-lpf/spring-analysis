package com.axon.springframework.beans.factory;

/**
 *  感知BeanFactory
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);
}
