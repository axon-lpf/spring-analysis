package com.bugstack.springframework.beans.factory.support;

import com.bugstack.springframework.beans.factory.FactoryBean;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.config.BeanPostProcessor;
import com.bugstack.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.bugstack.springframework.core.covert.ConversionService;
import com.bugstack.springframework.utils.ClassUtils;
import com.bugstack.springframework.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;


/**
 * bean工厂用来获取实例， 获取不到则去创建实例
 * <p>
 * 抽象bean工厂，实现获取bean的实现。 抽象方法 获取beanDefinition和创建bean
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {


    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();


    private ConversionService conversionService;


    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        //去缓存中去寻找，
        return doGetBean(beanName, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T) getBean(name);
    }


    protected <T> T doGetBean(String beanName, Object... args) {
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Object bean = createBean(beanName, beanDefinition, args);
        return (T) getObjectForBeanInstance(bean, beanName);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }
        Object object = getCachedObjectForFactoryBean(beanName);
        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);

        }
        return object;

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

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }


    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveValue(result);
        }
        return result;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    protected abstract boolean containsBeanDefinition(String beanName);

}
