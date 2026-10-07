package com.burkina.messenger.domain.entity;

import com.burkina.common.enums.messenger.ChatType;
import jakarta.persistence.*;
import lombok.*;
import com.burkina.messenger.domain.entity.id.ChatId;

@Entity
@Table(name = "chats")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChatId.class)
public class Chat {

    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatType chatType;

    @Column
    private String name;

    @Column
    private Long avatarId;
}
