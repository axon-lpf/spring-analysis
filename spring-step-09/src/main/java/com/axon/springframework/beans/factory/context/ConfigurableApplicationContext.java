package com.axon.springframework.beans.factory.context;

public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     */
    void refresh();

    void registerShutdownHook();

    void close();
}
