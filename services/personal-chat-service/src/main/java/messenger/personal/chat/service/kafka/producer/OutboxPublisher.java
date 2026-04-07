package messenger.personal.chat.service.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.personal.chat.service.service.outbox.OutboxEvent;
import messenger.personal.chat.service.service.outbox.OutboxEventService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

        for (OutboxEvent event : events) {
            kafkaTemplate.send(
                    event.getTopic(),
                    event.getId().toString(),
                    event.getPayload()
            ).whenComplete((res, ex) -> {
                if (ex == null) {
                    outboxEventService.markEventSent(event);
                } else {
                    log.error("Failed to send event {} to Kafka", event.getId(), ex);
                }
            });
        }
    }
}