package messenger.user.service.service.outbox;

import enums.UserEventType;
import lombok.RequiredArgsConstructor;
import messenger.user.service.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void saveEvent(String topic, UserEventType eventType, User user) {
        OutboxEvent event = OutboxEvent.builder()
                .topic(topic)
                .eventType(eventType)
                .user(user)
                .createdAt(Instant.now())
                .build();

        outboxEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public List<OutboxEvent> fetchPendingEvents() {
        return outboxEventRepository.findPendingEvents(PageRequest.of(0, 100));
    }

    @Transactional
    public void markEventSent(OutboxEvent event) {
        event.setSent(true);
        outboxEventRepository.save(event);
    }
}
