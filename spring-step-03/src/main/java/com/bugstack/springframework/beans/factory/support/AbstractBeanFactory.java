package com.bugstack.springframework.beans.factory.support;

import com.bugstack.springframework.beans.factory.BeanFactory;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;


/**
 * bean工厂用来获取实例， 获取不到则去创建实例
 * <p>
 * 抽象bean工厂，实现获取bean的实现。 抽象方法 获取beanDefinition和创建bean
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        //去缓存中去寻找，
        Object singletonBean = getSingleton(beanName);
        if (singletonBean != null) {
            System.out.println("从缓存中找到bean了。。。。。");
            return singletonBean;
        }
        System.out.println("找不到bean，我去创建了。。。。。");
        //找不到去创建
        //获取beanDefinition
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        //创建bean实例
        return createBean(beanName, beanDefinition);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        //去缓存中去寻找，
        Object singletonBean = getSingleton(beanName);
        if (singletonBean != null) {
            System.out.println("从缓存中找到bean了。。。。。");
            return singletonBean;
        }
        System.out.println("找不到bean，我去创建了。。。。。");
        //找不到去创建
        //获取beanDefinition
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        //创建bean实例
        return createBeanInstance(beanName, beanDefinition, args);
    }


    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);


    protected abstract Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args);

}
