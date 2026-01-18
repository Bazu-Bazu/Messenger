package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messenger.message.service.domain.entity.Message;
import enums.ChatType;
import messenger.message.service.domain.repository.MessageRepository;
import dto.response.MessageResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageCacheService {

    private final MessageRepository messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final int PAGE_SIZE = 50;
    private final String CACHE_NAME = "chatMessages";

    @Cacheable(value = CACHE_NAME, key = "#p0 + ':' + #p1 + ':' + #p2")
    public List<MessageResponse> getCachedMessages(Long chatId, ChatType chatType, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("createdAt").descending());
        return messageRepository.findByChatIdAndChatType(chatId, chatType, pageable).stream()
                .map(this::createMessageResponse)
                .toList();
    }

    public void evictChatMessages(Long chatId, ChatType chatType) {
        String pattern = CACHE_NAME + "::" + chatId + ":" + chatType + ":*";

        Set<String> keys = redisTemplate.keys(pattern);

        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
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
