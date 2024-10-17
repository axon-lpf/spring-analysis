package com.axon.springframework.step10.example;

import com.axon.springframework.beans.factory.context.ApplicationEvent;
import com.axon.springframework.beans.factory.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

public class StockChanged {
}


// 事件类
 class StockChangedEvent extends ApplicationEvent {
    private final String productId;
    private final int newStock;

    public StockChangedEvent(Object source, String productId, int newStock) {
        super(source);
        this.productId = productId;
        this.newStock = newStock;
    }

    public String getProductId() {
        return productId;
    }

    public int getNewStock() {
        return newStock;
    }
}

// 事件发布者
@Component
 class InventoryService {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void updateStock(String productId, int newStock) {
        // 更新库存逻辑
        publisher.publishEvent(new StockChangedEvent(this, productId, newStock));
    }
}

// 事件监听器
@Component
 class NotificationService {
    @EventListener
    public void handleStockChangedEvent(StockChangedEvent event) {
        // 发送库存变动通知逻辑
        System.out.println("Stock for product " + event.getProductId() + " changed to " + event.getNewStock());
    }
}
