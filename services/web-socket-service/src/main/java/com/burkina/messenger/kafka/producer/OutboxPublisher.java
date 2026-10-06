package com.burkina.messenger.kafka.producer;

import com.burkina.messenger.outbox.entity.OutboxEvent;
import com.burkina.messenger.outbox.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class OutboxPublisher {

    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 3000)
    public void publishEvents() {
        List<OutboxEvent> events = outboxEventService.fetchPendingEvents();

        List<Long> successIds = new ArrayList<>();
        List<Long> failedIds = new ArrayList<>();

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                        event.getTopic(),
                        event.getId().toString(),
                        event.getPayload()
                ).get();

                successIds.add(event.getId());
            } catch (Exception e) {
                failedIds.add(event.getId());

                log.error("Failed to send event {} to Kafka", event.getId(), e);
            }
        }

        outboxEventService.finishPublishing(successIds, failedIds);
    }
}