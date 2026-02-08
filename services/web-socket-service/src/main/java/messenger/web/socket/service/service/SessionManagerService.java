package messenger.web.socket.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SessionManagerService {

    private final StringRedisTemplate stringRedisTemplate;
    private final Map<String, WebSocketSession> localSessions = new ConcurrentHashMap<>();
    private final String USER_SESSION_KEY = "user:%s:sessions";
    private static final String SESSION_USER_KEY = "session:%s:user";

    public void addSession(WebSocketSession session, Long userId) {
        String sessionId = session.getId();

        localSessions.put(sessionId, session);

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
        localSessions.remove(sessionId);

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

    public Set<WebSocketSession> getSessionsByUsersId(Set<Long> participantIds) {
        Set<WebSocketSession> sessions = new HashSet<>();

        for (Long userId : participantIds) {
            Set<String> sessionIds = stringRedisTemplate.opsForSet().members(
                    String.format(USER_SESSION_KEY, userId)
            );

            if (sessionIds != null) {
                for (String sessionId : sessionIds) {
                    WebSocketSession session = localSessions.get(sessionId);
                    if (session != null && session.isOpen()) {
                        sessions.add(session);
                    }
                }
            }
        }

        return sessions;
    }

}
