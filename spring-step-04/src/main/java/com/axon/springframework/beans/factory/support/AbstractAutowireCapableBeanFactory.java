package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.PropertyValue;
import com.axon.springframework.beans.factory.PropertyValues;
import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建bean的核心类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {


    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {

        Object bean = null;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        applyPropertyValues(beanName, bean, beanDefinition);
        registerSingletonBean(beanName, bean);
        return bean;
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        // 创建bean实例
        Object bean = createBeanInstance(beanName, beanDefinition, args);
        // 设置属性
        applyPropertyValues(beanName, bean, beanDefinition);
        registerSingletonBean(beanName, bean);
        return bean;
    }

    @Override
    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        //getDeclaredConstructors() 获取所有的构造函数
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor : declaredConstructors) {
            if (null != args && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        try {
            return instantiationStrategy.instantiate(beanDefinition, beanName, constructorToUse, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置bean的属性
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    @Override
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            PropertyValue[] valuesPropertyValues = propertyValues.getPropertyValues();
            for (PropertyValue propertyValue : valuesPropertyValues) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if (value instanceof BeanReference) {

                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                //属性填充
                Class<?> aClass = bean.getClass();
                Field declaredField = aClass.getDeclaredField(name);
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
