package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {

        Object object = this.factoryBeanObjectCache.get(beanName);
        return object != null ? object : null;
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        if (factoryBean.isSingleton()) {
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                this.factoryBeanObjectCache.put(beanName, object);
                return object != null ? object : null;

            } else {
                return doGetObjectFromFactoryBean(factoryBean, beanName);
            }
        }
        return null;
    }

    private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
