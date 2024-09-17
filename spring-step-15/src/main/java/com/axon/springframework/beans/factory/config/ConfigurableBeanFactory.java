package com.axon.springframework.beans.factory.config;

import com.axon.springframework.beans.factory.HierarchicalBeanFactory;
import com.axon.springframework.utils.StringValueResolver;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例对象
     */
    void destroySingletons();



    void addEmbeddedValueResolver(StringValueResolver valueResolver);


    String resolveEmbeddedValue(String value);


}
