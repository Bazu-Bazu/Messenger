package messenger.message.service.service;

import enums.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageCacheInvalidationService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_NAME = "chatMessages";

    @Async
    public void evictChatMessages(Long chatId, ChatType chatType) {
        String pattern = CACHE_NAME + "::" + chatId + ":" + chatType + ":*";

        Set<String> keys = redisTemplate.keys(pattern);

        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
