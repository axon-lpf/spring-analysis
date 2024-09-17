package com.axon.springframework.beans.factory.context.support;

import java.util.Map;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {

    }


    public ClassPathXmlApplicationContext(String configLocations) {
        this(new String[]{configLocations});
    }


    /**
     *   从 xml 文件中加载 BeanDefinition,并刷新上下文
     * @param configLocations
     */
    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }


    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
