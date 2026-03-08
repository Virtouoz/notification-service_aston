package com.learn.notificationservice.dto.event;

public record UserEvent(String operation, String email, Long userId) {}