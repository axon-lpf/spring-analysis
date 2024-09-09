package com.bugstack.springframework.step10;

import com.bugstack.springframework.beans.factory.context.ApplicationListener;
import com.bugstack.springframework.beans.factory.context.event.ContextClosedEvent;

/**
 *  关闭事件
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }
}
