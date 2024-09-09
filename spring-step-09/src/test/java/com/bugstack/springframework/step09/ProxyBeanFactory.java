package com.bugstack.springframework.step09;

import com.bugstack.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


public class ProxyBeanFactory implements FactoryBean<IUserDao> {
    @Override
    public IUserDao getObject() throws Exception {

        InvocationHandler invocationHandler = (proxy, method, args) -> {
            Map<String, String> map = new HashMap<>();
            map.put("1", "收款单上岛咖啡");
            map.put("2", "我虞城县");
            map.put("3", "阿毛");

            return "你被代理了" + method.getName() + ":" + map.get(args[0].toString());
        };
        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserDao.class}, invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

