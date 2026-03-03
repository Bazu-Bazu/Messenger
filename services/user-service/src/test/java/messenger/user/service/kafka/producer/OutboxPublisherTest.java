package messenger.user.service.kafka.producer;

import dto.event.UserEvent;
import enums.UserEventType;
import messenger.user.service.domain.entity.User;
import messenger.user.service.service.outbox.OutboxEvent;
import messenger.user.service.service.outbox.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OutboxPublisherTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private OutboxPublisher outboxPublisher;

    private OutboxEvent event;

    @BeforeEach
    void setUp() {
        event = OutboxEvent.builder()
                .id(1L)
                .topic("user-event")
                .sent(false)
                .user(User.builder()
                        .id(1L)
                        .username("john")
                        .phone("+123456789")
                        .email("john@example.com")
                        .password("secret")
                        .build())
                .eventType(UserEventType.USER_REGISTERED)
                .build();
    }

    @Test
    void publishEvents_shouldSendEventsAndMarkAsSent() throws Exception {
        when(outboxEventRepository.findTop100BySentFalseOrderByCreatedAtAsc())
                .thenReturn(List.of(event));

        CompletableFuture mockFuture = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(anyString(), anyString(), any(UserEvent.class)))
                .thenReturn(mockFuture);

        outboxPublisher.publishEvents();

        verify(kafkaTemplate).send(eq("user-event"), eq(event.getId().toString()), any(UserEvent.class));
        assertTrue(event.isSent());
    }

    @Test
    void publishEvents_shouldNotFail_whenKafkaThrowsException() throws Exception {
        when(outboxEventRepository.findTop100BySentFalseOrderByCreatedAtAsc())
                .thenReturn(List.of(event));

        CompletableFuture mockFuture = new CompletableFuture<>();
        mockFuture.completeExceptionally(new RuntimeException("Kafka down"));

        when(kafkaTemplate.send(anyString(), anyString(), any(UserEvent.class)))
                .thenReturn(mockFuture);

        assertDoesNotThrow(() -> outboxPublisher.publishEvents());

        assertFalse(event.isSent());
    }
}