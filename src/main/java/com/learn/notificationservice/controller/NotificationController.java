package com.learn.notificationservice.controller;

import com.learn.notificationservice.dto.request.SendNotificationRequest;
import com.learn.notificationservice.notification.NotificationService;
import com.learn.notificationservice.notification.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "API для отправки уведомлений")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Отправить уведомление вручную",
            description = "Поддерживает CREATE / DELETE")
    @ApiResponse(responseCode = "200", description = "Уведомление отправлено")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @PostMapping("/send")
    public ResponseEntity<String> send(@Valid @RequestBody SendNotificationRequest request) {
        NotificationType type = NotificationType.valueOf(request.operation().toUpperCase());
        notificationService.send(type, request.email());
        return ResponseEntity.ok("Notification sent successfully");
    }
}