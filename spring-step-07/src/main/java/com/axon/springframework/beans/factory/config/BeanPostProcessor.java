package com.axon.springframework.beans.factory.config;

public interface BeanPostProcessor {

    /**
     * 在对象执行初始化之前，执行此法
     *
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean, String beanName);


    /**
     * 在对象执行初始化之前，执行此法
     *
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean, String beanName);
}
