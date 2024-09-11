package com.bugstack.springframework.beans.factory.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.bugstack.springframework.beans.factory.config.BeanDefinition;
import com.bugstack.springframework.stereotype.Component;
import com.bugstack.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
         classes = ClassUtil.scanPackageByAnnotation(basePackage, Service.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }

        return candidates;
    }
}
