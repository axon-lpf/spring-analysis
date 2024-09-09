package com.bugstack.springframework.beans.factory.context.event;

import com.bugstack.springframework.beans.factory.context.ApplicationContext;
import com.bugstack.springframework.beans.factory.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public  final ApplicationContext getApplicationContext() {
        return (ApplicationContext) super.getSource();
    }
}
