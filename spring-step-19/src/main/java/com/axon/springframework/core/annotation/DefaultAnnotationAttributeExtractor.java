package com.axon.springframework.core.annotation;


import com.axon.springframework.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class DefaultAnnotationAttributeExtractor extends AbstractAliasAwareAnnotationAttributeExtractor<Annotation> {

    DefaultAnnotationAttributeExtractor(Annotation annotation, Object annotatedElement) {
        super(annotation.annotationType(), annotatedElement, annotation);
    }

    @Override
    protected Object getRawAttributeValue(Method attributeMethod) {
        ReflectionUtils.makeAccessible(attributeMethod);
        return ReflectionUtils.invokeMethod(attributeMethod, getSource());
    }

    @Override

    protected Object getRawAttributeValue(String attributeName) {
        Method attributeMethod = ReflectionUtils.findMethod(getAnnotationType(), attributeName);
        return (attributeMethod != null ? getRawAttributeValue(attributeMethod) : null);
    }
}
