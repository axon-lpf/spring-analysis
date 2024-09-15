package com.bugstack.springframework.aop;

/**
 *  用于获取ClassFilter、MethodMatcher类
 */
public interface Pointcut {


    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();


}
