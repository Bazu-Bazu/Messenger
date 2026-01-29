package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheEvictionService {

    private final CacheManager cacheManager;
    public static final String GROUP_CHAT_CACHE = "groupChat";
    public static final String GROUP_MEMBERS_CACHE = "groupMembers";
    public static final String USER_GROUP_CHATS_CACHE = "userGroupChats";

    public void evictGroupChatCache(Long groupId) {
        evict(GROUP_CHAT_CACHE, groupId.toString());
    }

    public void evictGroupMembersCache(Long groupId) {
        evict(GROUP_MEMBERS_CACHE, groupId.toString());
    }

    public void evictUserGroupChats(Long userId) {
        evict(USER_GROUP_CHATS_CACHE, userId.toString());
    }

    private void evict(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

}
