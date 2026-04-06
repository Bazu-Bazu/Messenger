package messenger.chat.service.domain.entity.id;

import enums.ChatType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class ChatId {

    private Long chatId;
    private ChatType chatType;
}
