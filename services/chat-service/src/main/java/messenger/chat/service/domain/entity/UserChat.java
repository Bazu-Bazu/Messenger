package messenger.chat.service.domain.entity;

import enums.ChatMemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "user_chats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "chat_id", "chat_type"})
        }
)
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "chat_id", referencedColumnName = "chatId", nullable = false),
            @JoinColumn(name = "chat_type", referencedColumnName = "chatType", nullable = false)
    })
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatMemberRole role;

    private Long lastMessageId;
    private String lastMessageText;
    private Instant lastMessageTime;
    private Integer unreadCount;
}
