package messenger.web.socket.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionManagerService {

    private final StringRedisTemplate stringRedisTemplate;
    private final String USER_SESSION_KEY = "user:%s:sessions";
    private static final String SESSION_USER_KEY = "session:%s:user";

    public void addSession(String sessionId, Long userId) {
        stringRedisTemplate.opsForValue().set(
                String.format(SESSION_USER_KEY, sessionId),
                userId.toString()
        );

        stringRedisTemplate.opsForSet().add(
                String.format(USER_SESSION_KEY, userId),
                sessionId
        );
    }

    public void removeSession(String sessionId) {
        String userIdStr = stringRedisTemplate.opsForValue().get(
                String.format(SESSION_USER_KEY, sessionId)
        );

        if (userIdStr != null) {
            Long userId = Long.parseLong(userIdStr);

            stringRedisTemplate.opsForSet().remove(
                    String.format(USER_SESSION_KEY, userId),
                    sessionId
            );
        }

        stringRedisTemplate.delete(String.format(SESSION_USER_KEY, sessionId));
    }

    public Long getUserId(String sessionId) {
        String userIdStr = stringRedisTemplate.opsForValue().get(
                String.format(SESSION_USER_KEY, sessionId)
        );

        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }

}
