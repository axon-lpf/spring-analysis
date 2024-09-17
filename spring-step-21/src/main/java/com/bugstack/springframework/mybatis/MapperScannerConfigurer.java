package com.bugstack.springframework.mybatis;

import cn.hutool.core.lang.ClassScanner;
import com.bugstack.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.bugstack.springframework.beans.factory.PropertyValue;
import com.bugstack.springframework.beans.factory.PropertyValues;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.bugstack.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import com.bugstack.springframework.mybatis.middleware.SqlSessionFactory;

import java.util.Set;


/**
 *
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        try {
            Set<Class<?>> classes = ClassScanner.scanPackage(basePackage); // 扫描包
            for (Class<?> clazz : classes) {
                // Bean 对象定义
                BeanDefinition beanDefinition = new BeanDefinition(clazz);
                PropertyValues propertyValues = new PropertyValues();
                propertyValues.addPropertyValue(new PropertyValue("mapperInterface", clazz));   // 注入实际的接口类型。方便MapperFactoryBean 去创建代理对象
                propertyValues.addPropertyValue(new PropertyValue("sqlSessionFactory", sqlSessionFactory));  //注入数据库连接工厂， 方便MapperFactoryBean去操作数据库
                beanDefinition.setPropertyValues(propertyValues);
                //根据指定的Dao接口设置实际的BeanClass类型，最终在创建bean的时候，实例化的是MapperFactoryBean，
                //通MapperFactoryBean 的getObject 获取一个代理对象，然后执行数据库的访问方法。
                beanDefinition.setBeanClass(MapperFactoryBean.class);
                // Bean 对象注册
                registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // left intentionally blank
    }
}
