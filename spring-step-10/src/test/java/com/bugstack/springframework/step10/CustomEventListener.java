package com.bugstack.springframework.step10;

import com.bugstack.springframework.beans.factory.context.ApplicationListener;

import java.util.Date;

/**
 * 自定义事件
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
        System.out.println("消息：" + event.getId() + ":" + event.getMessage());
    }
}
