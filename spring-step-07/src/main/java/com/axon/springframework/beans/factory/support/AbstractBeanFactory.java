package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.BeanPostProcessor;
import com.axon.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * bean工厂用来获取实例， 获取不到则去创建实例
 * <p>
 * 抽象bean工厂，实现获取bean的实现。 抽象方法 获取beanDefinition和创建bean
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {


    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    @Override
    public Object getBean(String beanName) {
        //去缓存中去寻找，
        Object singletonBean = getSingleton(beanName);
        if (singletonBean != null) {
            return singletonBean;
        }
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
            return singletonBean;
        }
        //找不到去创建
        //获取beanDefinition
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        //创建bean实例
        Object beanInstance = createBeanInstance(beanName, beanDefinition, args);

        return beanInstance;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T) getBean(name);
    }


    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);


    protected abstract Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args);


    protected abstract void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition);


    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }


}
