package com.axon.springframework.aop;

public class TargetSource {

    private final Object target;

    public Object getTarget() {
        return target;
    }

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?>[] getTargetClass() {
        return this.target.getClass().getInterfaces();
    }

}
