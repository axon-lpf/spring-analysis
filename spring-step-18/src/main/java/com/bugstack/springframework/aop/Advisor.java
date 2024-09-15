package com.bugstack.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 *  定义一个访问者
 */
public interface Advisor {

    Advice getAdvice();
}
