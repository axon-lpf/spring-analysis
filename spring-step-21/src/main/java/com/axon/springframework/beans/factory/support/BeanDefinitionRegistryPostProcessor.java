package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.config.BeanFactoryPostProcessor;


public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {


    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);

}
