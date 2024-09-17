package com.axon.springframework.beans.factory.context.event;

import com.axon.springframework.beans.factory.BeanFactory;
import com.axon.springframework.beans.factory.context.ApplicationEvent;
import com.axon.springframework.beans.factory.context.ApplicationListener;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {

        for (ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }
}
