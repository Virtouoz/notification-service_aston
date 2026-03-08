package com.learn.notificationservice.config;

import com.learn.notificationservice.notification.NotificationType;
import com.learn.notificationservice.notification.strategy.AccountCreatedStrategy;
import com.learn.notificationservice.notification.strategy.AccountDeletedStrategy;
import com.learn.notificationservice.notification.strategy.NotificationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class NotificationConfig {

    @Bean
    public Map<NotificationType, NotificationStrategy> notificationStrategies(
            AccountCreatedStrategy created,
            AccountDeletedStrategy deleted) {

        return Map.of(
                NotificationType.CREATE, created,
                NotificationType.DELETE, deleted
        );
    }
}