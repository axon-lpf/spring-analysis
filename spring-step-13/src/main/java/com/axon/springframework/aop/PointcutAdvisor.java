package com.axon.springframework.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
