package com.learn.notificationservice.kafka;

import com.learn.notificationservice.event.UserEvent;
import com.learn.notificationservice.notification.NotificationService;
import com.learn.notificationservice.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consume(UserEvent event) {
        try {
            NotificationType type = NotificationType.valueOf(event.operation().toUpperCase());
            notificationService.send(type, event.email());
            log.info("✅ Уведомление отправлено: {} → {}", event.operation(), event.email());
        } catch (Exception e) {
            log.error("❌ Ошибка обработки события: {}", event, e);
            // В продакшене → отправить в Dead Letter Topic
        }
    }
}