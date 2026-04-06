package messenger.chat.service.service;

import enums.ChatType;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.domain.entity.Chat;
import messenger.chat.service.domain.repository.ChatRepository;
import messenger.chat.service.exception.ChatNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    public Chat findChatByIdAndType(Long chatId, ChatType chatType) {
        return chatRepository.findChatByChatIdAndChatType(chatId, chatType)
                .orElseThrow(() -> new ChatNotFoundException(
                            String.format("%s chat %d not found", chatType, chatId)
                ));
    }
}
