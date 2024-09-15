package com.bugstack.springframework.beans.factory.context;

import java.util.EventObject;

public abstract class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }
}
