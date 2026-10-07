package com.burkina.messenger.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column
    @Builder.Default
    private Boolean canSend = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
