package com.axon.springframework.core.covert.converter;

/**
 * 类型转换的注册接口
 */
public interface ConverterRegistry {

    void addConverter(Converter<?, ?> converter);

    void addConverter(GenericConverter converter);

    void addConverterFactory(ConverterFactory<?, ?> converter);
}
