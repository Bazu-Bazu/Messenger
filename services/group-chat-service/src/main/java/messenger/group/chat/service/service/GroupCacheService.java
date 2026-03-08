package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupCacheService {

    private final CacheManager cacheManager;

    public static final String USER_GROUP_CHATS_CACHE = "userGroupChats";

    @Async
    public void evictUsersChats(List<Long> userIds) {
        userIds.forEach(userId -> evict(USER_GROUP_CHATS_CACHE, userId.toString()));
    }

    private void evict(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }
}
