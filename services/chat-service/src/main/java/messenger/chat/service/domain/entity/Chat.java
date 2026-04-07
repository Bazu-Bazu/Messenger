package messenger.chat.service.domain.entity;

import enums.ChatType;
import jakarta.persistence.*;
import lombok.*;
import messenger.chat.service.domain.entity.id.ChatId;

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

    @Id
    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    private String title;
    private Long avatarId;
}
