package com.bugstack.springframework.beans.factory.support;

import com.bugstack.springframework.beans.factory.config.BeanFactoryPostProcessor;


public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {


    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);

}
