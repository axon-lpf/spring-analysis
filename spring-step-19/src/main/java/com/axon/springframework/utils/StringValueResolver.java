package com.axon.springframework.utils;

/**
 * 用于解析字符串的接口
 */
public interface StringValueResolver {

    String resolveValue(String value);

}
