package com.bugstack.springframework.core.covert.converter;


/**
 * 类型转换接口
 *
 * @param <S>
 * @param <T>
 */
public interface Converter<S, T> {

    T convert(S source);
}
