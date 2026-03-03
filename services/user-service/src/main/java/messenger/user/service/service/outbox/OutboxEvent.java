package messenger.user.service.service.outbox;

import enums.UserEventType;
import jakarta.persistence.*;
import lombok.*;
import messenger.user.service.domain.entity.User;

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
    @Enumerated(EnumType.STRING)
    private UserEventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private boolean sent = false;

    @Column(nullable = false)
    private Instant createdAt;
}
