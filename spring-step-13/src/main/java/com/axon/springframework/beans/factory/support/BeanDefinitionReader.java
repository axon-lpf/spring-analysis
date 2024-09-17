package com.axon.springframework.beans.factory.support;

import com.axon.springframework.core.io.Resource;
import com.axon.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource);


    void loadBeanDefinitions(Resource... resource);


    void loadBeanDefinitions(String location);


    void loadBeanDefinitions(String... locations);


}
