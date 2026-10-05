package com.burkina.messenger.domain.entity;

import com.burkina.common.enums.messenger.ChatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "chat_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"chatId", "chatType", "userId"})
        }
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType chatType;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean canSendMessage;

    @Column
    private Long lastReadMessageId;

    @Column(nullable = false)
    @Builder.Default
    private Instant joinedAt = Instant.now();

    public void serCanSendMessage(boolean canSendMessage) {
        this.canSendMessage = canSendMessage;
    }
}
