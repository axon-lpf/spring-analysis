package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类获取和注册单列bean
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String, Object> singletonMap = new HashMap<>();


    @Override
    public Object getSingleton(String name) {
        return singletonMap.get(name);
    }

    @Override
    public void registerSingletonBean(String beanName, Object singletonBean) {
        singletonMap.put(beanName, singletonBean);
    }

}
