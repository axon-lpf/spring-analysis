package com.bugstack.springframework.step10;

import com.bugstack.springframework.beans.factory.context.ApplicationListener;
import com.bugstack.springframework.beans.factory.context.event.ContextRefreshedEvent;

/**
 * 刷新事件
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件：" + this.getClass().getName());
    }
}
