package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import messenger.message.service.domain.entity.Message;
import messenger.message.service.domain.enums.ChatType;
import messenger.message.service.domain.repository.MessageRepository;
import messenger.message.service.dto.response.MessageResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageCacheService {

    private final MessageRepository messageRepository;
    private final int PAGE_SIZE = 50;

    @Cacheable(value = "chatMessages", key = "#chatId + ':' + #chatType + ':' + #page")
    public List<MessageResponse> getCachedMessages(Long chatId, ChatType chatType, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        return messageRepository.findByChatIdAndChatType(chatId, chatType, pageable).stream()
                .map(this::createMessageResponse)
                .toList();
    }

    private MessageResponse createMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .chatId(message.getChatId())
                .createdAt(message.getCreatedAt())
                .editedAt(message.getEditedAt())
                .readAt(message.getReadAt())
                .messageType(message.getMessageType())
                .chatType(message.getChatType())
                .build();
    }

}
