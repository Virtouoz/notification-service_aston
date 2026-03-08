package com.learn.notificationservice.notification.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDeletedStrategy implements NotificationStrategy {

    private final JavaMailSender mailSender;

    @Override
    public void send(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Аккаунт удалён");
        msg.setText("Здравствуйте!\n\nВаш аккаунт был удалён.");
        mailSender.send(msg);
    }
}
