package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(String toEmail, Long orderId, double amount) {

        try {
            System.out.println("🚀 Trying to send email to: " + toEmail);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(toEmail);
            message.setSubject("Order Confirmed #" + orderId);
            message.setText("Order placed successfully");

            mailSender.send(message);

            System.out.println("✅ EMAIL SENT SUCCESSFULLY");

        } catch (Exception e) {
            System.out.println("❌ EMAIL FAILED: " + e.getMessage());
        }
    }
}