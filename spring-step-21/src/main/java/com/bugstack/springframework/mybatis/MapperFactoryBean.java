package com.bugstack.springframework.mybatis;

import com.bugstack.springframework.beans.factory.FactoryBean;
import com.bugstack.springframework.mybatis.middleware.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 数据库的隐射工厂，每一个 DAO 接口对应 Mapper 的代理对象。通过代理对象完成数据库的操作。
 *
 * @param <T>
 */
public class MapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;
    private SqlSessionFactory sqlSessionFactory;

    public MapperFactoryBean() {
    }

    public MapperFactoryBean(Class<T> mapperInterface, SqlSessionFactory sqlSessionFactory) {
        this.mapperInterface = mapperInterface;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 这里去代理操作
     *
     * @return
     * @throws Exception
     */
    @Override
    public T getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {
            // 排除 Object 方法；toString、hashCode
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            }
            try {
                System.out.println("你被代理了，执行SQL操作！" + method.getName());
                return sqlSessionFactory.openSession().selectOne(mapperInterface.getName() + "." + method.getName(), args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return method.getReturnType().newInstance();
        };
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{mapperInterface}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
