package com.axon.springframework.core.annotation;

import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class AbstractAliasAwareAnnotationAttributeExtractor<S> implements AnnotationAttributeExtractor<S> {

    private final Class<? extends Annotation> annotationType;

    private final Object annotatedElement;

    private final S source;

    private final Map<String, List<String>> attributeAliasMap;

    public AbstractAliasAwareAnnotationAttributeExtractor(Class<? extends Annotation> annotationType, Object annotatedElement, S source) {
        this.annotationType = annotationType;
        this.annotatedElement = annotatedElement;
        this.source = source;
        this.attributeAliasMap = AnnotationUtils.getAttributeAliasMap(annotationType);

    }


    @Override
    public final Class<? extends Annotation> getAnnotationType() {
        return this.annotationType;
    }
    @Override
    public Object getAnnotatedElement() {
        return this.annotatedElement;
    }

    @Override
    public S getSource() {
        return this.source;
    }

    @Override
    public Object getAttributeValue(Method attributeMethod) {
        String attributeName = attributeMethod.getName();
        Object attributeValue = getRawAttributeValue(attributeMethod);

        List<String> aliasNames = this.attributeAliasMap.get(attributeName);
        if (aliasNames != null) {
            Object defaultValue = AnnotationUtils.getDefaultValue(this.annotationType, attributeName);
            for (String aliasName : aliasNames) {
                Object aliasValue = getRawAttributeValue(aliasName);

                if (!ObjectUtils.nullSafeEquals(attributeValue, aliasValue) &&
                        !ObjectUtils.nullSafeEquals(attributeValue, defaultValue) &&
                        !ObjectUtils.nullSafeEquals(aliasValue, defaultValue)) {
                    String elementName = (this.annotatedElement != null ? this.annotatedElement.toString() : "unknown element");
                    throw new AnnotationConfigurationException(String.format(
                            "In annotation [%s] declared on %s and synthesized from [%s], attribute '%s' and its " +
                                    "alias '%s' are present with values of [%s] and [%s], but only one is permitted.",
                            this.annotationType.getName(), elementName, this.source, attributeName, aliasName,
                            ObjectUtils.nullSafeToString(attributeValue), ObjectUtils.nullSafeToString(aliasValue)));
                }

                // If the user didn't declare the annotation with an explicit value,
                // use the value of the alias instead.
                if (ObjectUtils.nullSafeEquals(attributeValue, defaultValue)) {
                    attributeValue = aliasValue;
                }
            }
        }

        return attributeValue;
    }


    /**
     * Get the raw, unmodified attribute value from the underlying
     * {@linkplain #getSource source} that corresponds to the supplied
     * attribute method.
     */
    protected abstract Object getRawAttributeValue(Method attributeMethod);

    /**
     * Get the raw, unmodified attribute value from the underlying
     * {@linkplain #getSource source} that corresponds to the supplied
     * attribute name.
     */
    protected abstract Object getRawAttributeValue(String attributeName);
}
