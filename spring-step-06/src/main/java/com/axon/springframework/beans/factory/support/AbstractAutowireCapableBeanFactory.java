package com.axon.springframework.beans.factory.support;

import com.axon.springframework.beans.factory.PropertyValue;
import com.axon.springframework.beans.factory.PropertyValues;
import com.axon.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.axon.springframework.beans.factory.config.BeanDefinition;
import com.axon.springframework.beans.factory.config.BeanPostProcessor;
import com.axon.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 创建bean的核心类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {


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
        //注册bean的属性
        applyPropertyValues(beanName, bean, beanDefinition);

        //执行初始化方法 ,初始化前， 初始化后
        bean = initializeBean(beanName, bean, beanDefinition);

        //注册bean
        registerSingletonBean(beanName, bean);
        return bean;
    }

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object... args) {
        // 创建bean实例
        Object bean = createBeanInstance(beanName, beanDefinition, args);
        // 设置属性填充
        applyPropertyValues(beanName, bean, beanDefinition);

        //执行初始化方法 ,初始化前， 初始化后
        bean = initializeBean(beanName, bean, beanDefinition);

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

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 初始化前处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        // 执行初始化方法
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        // 初始化后处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;

    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

        System.out.println("正在执行初始化方法");
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (null == current) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (null == current) {
                return result;
            }
            result = current;
        }
        return result;
    }
}
