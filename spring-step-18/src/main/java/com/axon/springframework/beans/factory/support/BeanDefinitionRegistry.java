package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.config.BeanDefinition;

/**
 * 创建BeanDefinition的工厂
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);


    /**
     * 使用Bean名称查询BeanDefinition
     *
     * @param beanName
     * @return
     * @throws BeansException
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 判断是否包含指定名称的BeanDefinition
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * Return the names of all beans defined in this registry.
     *
     * 返回注册表中所有的Bean名称
     */
    String[] getBeanDefinitionNames();
}
