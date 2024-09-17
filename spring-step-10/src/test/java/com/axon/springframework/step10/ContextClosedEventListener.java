package com.axon.springframework.step10;

import com.axon.springframework.beans.factory.context.ApplicationListener;
import com.axon.springframework.beans.factory.context.event.ContextClosedEvent;

/**
 *  关闭事件
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }
}
