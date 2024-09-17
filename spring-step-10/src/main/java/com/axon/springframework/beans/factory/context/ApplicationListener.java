package com.axon.springframework.beans.factory.context;

import java.util.EventListener;

public interface  ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void  onApplicationEvent(E event);
}
