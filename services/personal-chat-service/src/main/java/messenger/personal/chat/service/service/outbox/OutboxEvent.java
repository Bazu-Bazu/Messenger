package messenger.personal.chat.service.service.outbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "outbox_events")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    @Builder.Default
    private boolean sent = false;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
