package messenger.personal.chat.service.service.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void saveEvent(String topic, String eventType, String aggregateType, String payload) {
        OutboxEvent event = OutboxEvent.builder()
                .topic(topic)
                .eventType(eventType)
                .aggregateType(aggregateType)
                .payload(payload)
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
