package com.axon.springframework.beans.factory.context.support;

import com.axon.springframework.beans.factory.FactoryBean;
import com.axon.springframework.beans.factory.InitializingBean;
import com.axon.springframework.core.covert.ConversionService;
import com.axon.springframework.core.covert.converter.Converter;
import com.axon.springframework.core.covert.converter.ConverterFactory;
import com.axon.springframework.core.covert.converter.ConverterRegistry;
import com.axon.springframework.core.covert.converter.GenericConverter;
import com.axon.springframework.core.covert.supoort.DefaultConversionService;
import com.axon.springframework.core.covert.supoort.GenericConversionService;

import java.util.Set;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;

    private GenericConversionService conversionService;

    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    /**
     * 初始化时注册到到容器中，将转换类型
     */
    @Override
    public void afterPropertiesSet() {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);

    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {

                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new RuntimeException("类型注册转换异常");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;

    }
}
