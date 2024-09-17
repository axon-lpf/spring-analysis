package com.axon.springframework.beans.factory.context.support;


import com.axon.springframework.beans.factory.config.BeanPostProcessor;
import com.axon.springframework.beans.factory.context.*;

/**
 * 由于并不能直接在创建bean时获取application属性，所以要在执行refresh时，写入一个包装的BeanPostProcessor类中
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
