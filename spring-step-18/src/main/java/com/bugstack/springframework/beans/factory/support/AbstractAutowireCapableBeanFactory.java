package com.bugstack.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.bugstack.springframework.beans.factory.*;
import com.bugstack.springframework.beans.factory.config.*;
import com.bugstack.springframework.core.covert.ConversionService;

import java.lang.reflect.Constructor;
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
    /*        // 判断是否参与了aop切面拦截，则需要代理创建。
            bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if (null != bean) {
                //创建代理对象后，不进行后面的实例化、属性填充、前置处理器的流程，直接返回代理对象
                return bean;
            }*/
            bean = beanDefinition.getBeanClass().newInstance();
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
        applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

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
        if (beanDefinition.isSingleton()) {
            Object finalBean = bean;
            //创建一个代理对象，添加到三级缓存中
            addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
        }

        // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
        applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

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
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        PropertyValue[] valuesPropertyValues = propertyValues.getPropertyValues();
        for (PropertyValue propertyValue : valuesPropertyValues) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getBeanName());
            } else {
                // 这里是对普通类型的转换.判断是否需要转换，如果需要，则进行转换
                Class<?> sourceType = value.getClass();
                Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);
                ConversionService conversionService = getConversionService();
                if (conversionService != null) {
                    if (conversionService.canConvert(sourceType, targetType)) {
                        value = conversionService.convert(value, targetType);
                    }
                }
            }
        /*    //属性填充,一个巨坑。 由于 CGLIB 创建的是代理类，可以通过代理类的父类（即原始类）来获取字段。使用 getSuperclass() 方法获取原始类，并从中获取字段进行操作。
            Class<?> aClass = ClassUtils.isCglibProxyClass(bean.getClass()) ? bean.getClass().getSuperclass() : bean.getClass();
            Field declaredField = aClass.getDeclaredField(name);
            declaredField.setAccessible(true);
            declaredField.set(bean, value);*/
            // 反射设置属性填充
            BeanUtil.setFieldValue(bean, name, value);
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

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        // 1. 尝试在实例化之前应用后置处理器
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        // 2. 如果实例化前后置处理器生成了一个对象（通常是代理对象）
        if (null != bean) {
            // 3. 直接调用初始化后置处理器，不再进行默认的实例化流程
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }

        return bean; // 返回代理对象或空
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (null != result) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if (null != pvs) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                if (null == exposedObject) {
                    return exposedObject;
                }
            }
        }
        return exposedObject;
    }
}
