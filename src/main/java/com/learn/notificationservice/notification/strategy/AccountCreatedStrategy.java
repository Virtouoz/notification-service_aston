package com.learn.notificationservice.notification.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCreatedStrategy implements NotificationStrategy {

    private final JavaMailSender mailSender;

    @Override
    public void send(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Аккаунт успешно создан");
        msg.setText("Здравствуйте!\n\nВаш аккаунт на сайте успешно создан.");
        mailSender.send(msg);
    }
}
