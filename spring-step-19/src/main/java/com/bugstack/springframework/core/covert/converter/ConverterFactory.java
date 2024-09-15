package com.bugstack.springframework.core.covert.converter;

/**
 * 类型转换工厂
 *
 * @param <S>
 * @param <R>
 */
public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> type);
}
