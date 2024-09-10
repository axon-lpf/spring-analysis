package com.bugstack.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.bugstack.springframework.beans.factory.*;
import com.bugstack.springframework.beans.factory.PropertyValues;
import com.bugstack.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import com.bugstack.springframework.beans.factory.config.BeanReference;
import com.bugstack.springframework.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        //注册实现DisposableBean 的接口
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

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

        //注册实现DisposableBean 的接口
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
        if (beanDefinition.isSingleton()) {
            //如果是单例则创建
            registerSingletonBean(beanName, bean);
        }
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
                //属性填充,一个巨坑。 由于 CGLIB 创建的是代理类，可以通过代理类的父类（即原始类）来获取字段。使用 getSuperclass() 方法获取原始类，并从中获取字段进行操作。
                Class<?> aClass = ClassUtils.isCglibProxyClass(bean.getClass()) ? bean.getClass().getSuperclass() : bean.getClass();
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

        //设置感知对象
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }
        // 初始化前处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        // 执行初始化方法
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        // 初始化后处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;

    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

        //获取InitializingBean 接口的bean
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        String initMethodName = beanDefinition.getInitMethodName();
        //处理通过配置文件 配置init-method的方法
        if (StrUtil.isNotEmpty(initMethodName)) {
            try {
                Method method = beanDefinition.getBeanClass().getMethod(initMethodName);
                if (null == method) {
                    throw new RuntimeException("没有找到对应的初始化方法");
                }
                method.invoke(wrappedBean);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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

    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {

        if (!beanDefinition.isSingleton()) {
            //非单例模式，则不进行销毁。
            return;
        }
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
