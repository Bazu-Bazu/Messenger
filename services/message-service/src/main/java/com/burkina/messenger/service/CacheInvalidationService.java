package com.burkina.messenger.service;

import com.burkina.common.enums.messenger.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CacheInvalidationService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String CHAT_MEMBER = "chatMember";
    private final static String CHAT_MEMBERS = "chatMembers";

    @Async
    public void evictChatMember(Long userId, Long chatId, ChatType chatType) {
        String key = CHAT_MEMBER + "::" + userId + ":" + chatId + ":" + chatType;

        try {
            redisTemplate.delete(key);
        } catch (RedisConnectionFailureException e) {
            log.warn(
                    "Failed to invalidate cache. userId={}, chatId={}, chatType={}",
                    userId,
                    chatId,
                    chatType,
                    e
            );
        }
    }

    @Async
    public void evictChatMembers(Long chatId, ChatType chatType) {
        String key = CHAT_MEMBERS + "::" + chatId + ":" + chatType;

        try {
            redisTemplate.delete(key);
        } catch (RedisConnectionFailureException e) {
            log.warn(
                    "Failed to invalidate cache. chatId={}, chatType={}",
                    chatId,
                    chatType,
                    e
            );
        }
    }
}
