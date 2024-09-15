package com.bugstack.springframework.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
