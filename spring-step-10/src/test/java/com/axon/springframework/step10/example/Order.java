package com.axon.springframework.step10.example;

import com.axon.springframework.beans.factory.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 *
 * 2. 异步处理
 * 通过事件，可以将某些操作异步执行，提升系统的性能和响应速度。
 *
 * 案例：订单创建后异步处理发货
 */
public class Order {
}


// 事件类
 class OrderCreatedEvent extends ApplicationEvent {
    private final String orderId;

    public OrderCreatedEvent(Object source, String orderId) {
        super(source);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

// 事件监听器
@Component
 class ShippingService {
    @EventListener
    @Async // 异步执行
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        // 处理发货逻辑
        System.out.println("Shipping order: " + event.getOrderId());
    }
}

