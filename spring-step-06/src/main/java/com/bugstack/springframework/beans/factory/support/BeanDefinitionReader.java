package com.bugstack.springframework.beans.factory.support;

import com.bugstack.springframework.core.io.Resource;
import com.bugstack.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource);


    void loadBeanDefinitions(Resource... resource);


    void loadBeanDefinitions(String location);


    void loadBeanDefinitions(String... locations);


}
