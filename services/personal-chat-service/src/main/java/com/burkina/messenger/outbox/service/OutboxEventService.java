package com.burkina.messenger.outbox.service;

import com.burkina.messenger.outbox.entity.OutboxEvent;
import com.burkina.messenger.outbox.enums.EventType;
import com.burkina.messenger.outbox.repository.OutboxEventCustomRepository;
import com.burkina.messenger.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventCustomRepository outboxEventCustomRepository;

    @Transactional
    public void saveEvent(EventType eventType, String payload) {
        String topic = eventType.getTopic();

        OutboxEvent event = OutboxEvent.builder()
                .topic(topic)
                .eventType(eventType)
                .payload(payload)
                .build();

        outboxEventRepository.save(event);
    }

    public List<OutboxEvent> fetchPendingEvents() {
        return outboxEventCustomRepository.reserveEvents(50);
    }

    @Transactional
    public void finishPublishing(List<Long> successIds, List<Long> failedIds) {
        if (!successIds.isEmpty()) {
            outboxEventRepository.markSent(successIds);
        }

        if (!failedIds.isEmpty()) {
            outboxEventRepository.markNew(failedIds);
        }
    }
}
