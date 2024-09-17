package com.axon.springframework.aop;


import org.aopalliance.intercept.MethodInterceptor;

public class AdvisedSupport {

    // 被代理的目标对象
    private  TargetSource targetSource;

    private MethodInterceptor interceptor;

    // 方法匹配器
    private  MethodMatcher methodMatcher;

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodInterceptor getMethodInterceptor() {
        return interceptor;
    }

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }
}
