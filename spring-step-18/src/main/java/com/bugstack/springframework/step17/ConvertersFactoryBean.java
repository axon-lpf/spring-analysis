package com.bugstack.springframework.step17;

import com.bugstack.springframework.beans.factory.FactoryBean;

import java.util.HashSet;
import java.util.Set;

/**
 * 转换工厂
 * FactoryBean 在这个阶段才理解， 如果容器有需要的属性是没有的， 比如ConversionServiceFactoryBean 中 converters 没有的
 * 则可以通过 FactoryBean 构建一个，放到容器中，去注入，
 * 因为在创建的bean过程中，会判断 当前的对象是否实现了FactoryBean ，如果实现了，则获取FactoryBean 的getObject注入到容器中
 *
 * factoryBean 则是对bean的扩展。
 *
 *      //先从三个缓存中找，找到了则去获取对应的factoryBean的扩展，
 *     Object sharedInstance = getSingleton(beanName);
 *         if (sharedInstance != null) {
 *             return (T) getObjectForBeanInstance(sharedInstance, beanName);
 *         }
 *         BeanDefinition beanDefinition = getBeanDefinition(beanName);
 *         Object bean = createBean(beanName, beanDefinition, args);
 *
 *         // 获取factoryBean的实例
 *         return (T) getObjectForBeanInstance(bean, beanName);
 *
 */
public class ConvertersFactoryBean implements FactoryBean<Set<?>> {

    @Override
    public Set<?> getObject() throws Exception {
        HashSet<Object> converters = new HashSet<>();
        StringToLocalDateConverter stringToLocalDateConverter = new StringToLocalDateConverter("yyyy-MM-dd");
        converters.add(stringToLocalDateConverter);
        return converters;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
