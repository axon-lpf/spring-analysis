package com.axon.springframework.beans.factory;

import com.axon.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

    void preInstantiateSingletons();
}
