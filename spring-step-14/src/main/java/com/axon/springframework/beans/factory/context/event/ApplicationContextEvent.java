package com.axon.springframework.beans.factory.context.event;

import com.axon.springframework.beans.factory.context.ApplicationContext;
import com.axon.springframework.beans.factory.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public  final ApplicationContext getApplicationContext() {
        return (ApplicationContext) super.getSource();
    }
}
