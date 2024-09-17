package com.axon.springframework.beans.factory.context.event;

import com.axon.springframework.beans.factory.context.ApplicationEvent;
import com.axon.springframework.beans.factory.context.ApplicationListener;

/**
 * 事件广播器
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加监听
     *
     * @param listener
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 删除监听
     *
     * @param listener
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 广播事件 ，推送时间消息
     *
     * @param event
     */
    void multicastEvent(ApplicationEvent event);

}
