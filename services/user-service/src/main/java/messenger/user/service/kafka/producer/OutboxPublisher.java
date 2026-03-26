package messenger.user.service.kafka.producer;

import dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.domain.entity.User;
import messenger.user.service.service.outbox.OutboxEvent;
import messenger.user.service.service.outbox.OutboxEventService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class OutboxPublisher {

    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    public void publishEvents() {
        List<OutboxEvent> events = outboxEventService.fetchPendingEvents();

        for (OutboxEvent outboxEvent : events) {
            UserEvent userEvent = createUserEvent(outboxEvent);

            try {
                kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getId().toString(), userEvent)
                        .get();

                outboxEventService.markEventSent(outboxEvent);
            }
            catch (Exception e) {
                log.error("Failed to send event {} to Kafka", outboxEvent.getId(), e);
            }
        }
    }

    private UserEvent createUserEvent(OutboxEvent event) {
        User user = event.getUser();

        return UserEvent.builder()
                .id(user.getId())
                .eventType(event.getEventType())
                .phone(user.getPhone())
                .password(user.getPassword())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatarId(user.getProfile() != null ? user.getProfile().getAvatarId() : 0L)
                .build();
    }
}
