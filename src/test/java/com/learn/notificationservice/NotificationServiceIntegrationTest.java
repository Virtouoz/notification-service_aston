package com.learn.notificationservice;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.learn.notificationservice.dto.request.SendNotificationRequest;
import com.learn.notificationservice.event.UserEvent;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NotificationServiceIntegrationTest {

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    private static GreenMail greenMail;

    @DynamicPropertySource
    static void configureMail(DynamicPropertyRegistry registry) {
        greenMail = new GreenMail(ServerSetup.SMTP.dynamicPort());
        greenMail.start();
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
    }

    @AfterAll
    static void tearDown() {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    @BeforeEach
    void purgeEmails() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();  // ← обязательно!
    }

    @Test
    void shouldSendCreateEmail_viaKafka() {
        kafkaTemplate.send("user-events", "test-create@example.com",
                new UserEvent("CREATE", "test-create@example.com", 1L));

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertThat(messages).hasSize(1);
            assertThat(messages[0].getSubject()).contains("создан");
        });
    }

    @Test
    void shouldSendDeleteEmail_viaKafka() {
        kafkaTemplate.send("user-events", "test-delete@example.com",
                new UserEvent("DELETE", "test-delete@example.com", 2L));

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertThat(messages).hasSize(1);
            assertThat(messages[0].getSubject()).contains("удалён");
        });
    }

    @Test
    void shouldSendEmail_viaRestApi() {
        // ← ИСПРАВЛЕНО: отправляем объект, а не строку → будет application/json
        var request = new SendNotificationRequest(
                "CREATE",
                "api-test@example.com"
        );

        var response = restTemplate.postForEntity(
                "/api/notifications/send",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        await().untilAsserted(() ->
                assertThat(greenMail.getReceivedMessages()).hasSize(1)
        );
    }
}