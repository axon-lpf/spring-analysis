package com.bugstack.springframework.aop;

/**
 *  定义接口匹配类
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);
}
