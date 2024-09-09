package com.bugstack.springframework.beans.factory.context.support;

import com.bugstack.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.bugstack.springframework.beans.factory.xml.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {

        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (null != configLocations) {
            xmlBeanDefinitionReader.loadBeanDefinitions(configLocations);
        }

    }

    /**
     * 该入口的目的是从上下文中类中获得配置信息
     *
     * @return
     */
    protected abstract String[] getConfigLocations();
}
