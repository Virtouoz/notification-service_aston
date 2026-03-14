package com.learn.notificationservice.controller;

import com.learn.notificationservice.assembler.NotificationModelAssembler;
import com.learn.notificationservice.dto.request.SendNotificationRequest;
import com.learn.notificationservice.dto.response.NotificationResponse;
import com.learn.notificationservice.notification.NotificationService;
import com.learn.notificationservice.notification.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
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
    private final NotificationModelAssembler assembler;

    @Operation(summary = "Отправить уведомление вручную",
            description = "Поддерживает CREATE / DELETE. Отправляет email через MailHog.")
    @ApiResponse(responseCode = "200", description = "Уведомление отправлено")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/send")
    public ResponseEntity<EntityModel<NotificationResponse>> send(@Valid @RequestBody SendNotificationRequest request) {
        NotificationType type = NotificationType.valueOf(request.operation().toUpperCase());
        notificationService.send(type, request.email());
        NotificationResponse response = new NotificationResponse("Notification sent successfully");
        return ResponseEntity.ok(assembler.toModel(response));
    }
}