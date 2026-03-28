package com.example.demo.messaging;

import com.example.demo.event.OrderEvent;
import com.example.demo.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "order.placed")
    public void consumeOrderEvent(OrderEvent event) {

        try {
            System.out.println("📩 Received order event: " + event.getOrderId());

            emailService.sendOrderConfirmation(
                    event.getUserEmail(),
                    event.getOrderId(),
                    event.getTotalAmount()
            );

            System.out.println("✅ Email processed successfully");

        } catch (Exception e) {
            // 🔥 VERY IMPORTANT (prevents UNACKED issue)
            System.out.println("❌ Error processing message: " + e.getMessage());
        }
    }
}