package com.learn.notificationservice.event;

public record UserEvent(
        String operation,
        String email,
        Long userId
) {
}