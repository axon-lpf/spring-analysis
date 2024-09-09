package com.bugstack.springframework.beans.factory.support;

import com.bugstack.springframework.beans.factory.config.BeanDefinition;

/**
 * 创建BeanDefinition的工厂
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
