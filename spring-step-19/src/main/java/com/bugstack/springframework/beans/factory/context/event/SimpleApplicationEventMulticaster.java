package com.bugstack.springframework.beans.factory.context.event;

import com.bugstack.springframework.beans.factory.BeanFactory;
import com.bugstack.springframework.beans.factory.context.ApplicationEvent;
import com.bugstack.springframework.beans.factory.context.ApplicationListener;

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
