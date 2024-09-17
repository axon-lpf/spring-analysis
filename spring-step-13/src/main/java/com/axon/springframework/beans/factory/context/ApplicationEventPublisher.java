package com.axon.springframework.beans.factory.context;

public interface  ApplicationEventPublisher {

    void  publishEvent(ApplicationEvent event);
}
