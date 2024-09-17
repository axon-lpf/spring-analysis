package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 *  spring 源码中的核心类
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("not beanName is defined");
        }
        return beanDefinition;
    }
}
