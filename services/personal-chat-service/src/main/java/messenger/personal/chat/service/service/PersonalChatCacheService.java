package messenger.personal.chat.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonalChatCacheService {

    private static final String CACHE_VALUE = "userPersonalChats";

    @Caching(evict = {
            @CacheEvict(value = CACHE_VALUE, key = "#p0"),
            @CacheEvict(value = CACHE_VALUE, key = "#p1")
    })
    public void evictUsersChats(Long user1Id, Long user2Id) {}
}
