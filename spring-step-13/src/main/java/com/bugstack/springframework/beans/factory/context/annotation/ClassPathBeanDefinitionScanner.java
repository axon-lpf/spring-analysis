package com.bugstack.springframework.beans.factory.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.bugstack.springframework.stereotype.Component;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {


    private BeanDefinitionRegistry registry;


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);

            for (BeanDefinition beanDefinition : candidateComponents) {
                String s = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(s)){
                    beanDefinition.setScope(s);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition),beanDefinition);
            }
        }
    }

    /**
     *  获取作用域的值
     * @param beanDefinition
     * @return
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope annotation = beanClass.getAnnotation(Scope.class);
        if (null != annotation) {
            return annotation.value();
        }
        return StrUtil.EMPTY;
    }


    private  String determineBeanName(BeanDefinition beanDefinition){
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }


}
