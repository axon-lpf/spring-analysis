package com.axon.springframework.step10.example;

import com.axon.springframework.beans.factory.context.ApplicationEvent;
import com.axon.springframework.beans.factory.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * 1. 解耦合组件
 * 当一个组件需要通知另一个组件，但又不想直接依赖于它时，可以使用事件。通过事件，发送者和接收者之间没有直接的耦合关系。
 *
 * 案例：用户注册后发送欢迎邮件
 *
 *
 */
public class User {
}


// 事件类
 class UserRegisteredEvent extends ApplicationEvent {
    private final String email;

    public UserRegisteredEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

// 事件发布者
@Component
 class UserService {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void registerUser(String email) {
        // 注册用户逻辑
        publisher.publishEvent(new UserRegisteredEvent(this, email));
    }
}

// 事件监听器
@Component
 class EmailService {
    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        // 发送欢迎邮件逻辑
        System.out.println("Sending welcome email to: " + event.getEmail());
    }
}

