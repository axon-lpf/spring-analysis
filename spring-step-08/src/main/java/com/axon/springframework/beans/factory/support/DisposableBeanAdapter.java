package com.axon.springframework.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.axon.springframework.beans.factory.DisposableBean;
import com.axon.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final String beanName;

    private final Object bean;

    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.beanName = beanName;
        this.bean = bean;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {

        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        if (StrUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean && "destory".equals(bean.getClass().getMethod(destroyMethodName)))) {
            Method method = bean.getClass().getMethod(destroyMethodName);
            if (null == method) {
                throw new RuntimeException("没有对应销毁方法");
            }
            method.invoke(bean);
        }
    }
}
