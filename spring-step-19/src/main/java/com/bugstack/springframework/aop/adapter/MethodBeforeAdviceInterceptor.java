package com.bugstack.springframework.aop.adapter;

import com.bugstack.springframework.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MethodBeforeAdviceInterceptor implements MethodInterceptor {


    public void setAdvice(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor() {
    }

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    /**
     * •	methodInvocation.getMethod()：获取被拦截的方法对象。
     * •	methodInvocation.getArguments()：获取方法的参数列表。
     * •	methodInvocation.getThis()：获取被拦截的目标对象。
     *
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //执行前置增强逻辑
        this.advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        //执行目标方法
        return methodInvocation.proceed();
    }
}
