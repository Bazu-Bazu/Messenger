package messenger.user.service.service.outbox;

import enums.UserEventType;
import lombok.RequiredArgsConstructor;
import messenger.user.service.domain.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;

    public void saveEvent(String topic, UserEventType eventType, User user) {
        OutboxEvent event = OutboxEvent.builder()
                .topic(topic)
                .eventType(eventType)
                .user(user)
                .createdAt(Instant.now())
                .build();

        outboxEventRepository.save(event);
    }
}
