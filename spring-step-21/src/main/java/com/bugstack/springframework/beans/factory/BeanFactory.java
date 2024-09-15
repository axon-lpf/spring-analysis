package com.bugstack.springframework.beans.factory;

public interface BeanFactory {

    Object getBean(String beanName) throws InstantiationException, IllegalAccessException;

    Object getBean(String beanName, Object... args);


    /**
     * 返回指定泛型的对象
     *
     * @param name         要检索的bean的名称
     * @param requiredType 类型
     * @param <T>          泛型
     * @return 实例化的 Bean 对象
     */
    <T> T getBean(String name, Class<T> requiredType);


    /**
     * 返回指定泛型的对象
     *
     * @param requiredType 类型
     * @param <T>          泛型
     * @return 实例化的 Bean 对象
     */
    <T> T getBean(Class<T> requiredType);


    /**
     * 是否保包含bean
     *
     * @param name
     * @return
     */
    boolean containsBean(String name);
}
