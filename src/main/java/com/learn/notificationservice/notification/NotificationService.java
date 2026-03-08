package com.learn.notificationservice.notification;

import com.learn.notificationservice.notification.strategy.NotificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Map<NotificationType, NotificationStrategy> strategies;

    public void send(NotificationType type, String email) {
        NotificationStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown notification type: " + type);
        }
        strategy.send(email);
    }
}
