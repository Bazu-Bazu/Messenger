package messenger.chat.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "chat_member")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMemberRole role;

    @Column(nullable = false)
    private Instant joinedAt;
    private Instant lastReadAt;

    @Column(nullable = false)
    private String userUsername;

}
