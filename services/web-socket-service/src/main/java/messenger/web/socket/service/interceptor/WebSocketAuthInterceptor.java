package messenger.web.socket.service.interceptor;

import lombok.RequiredArgsConstructor;
import messenger.web.socket.service.service.SessionManagerService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final SessionManagerService sessionManagerService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Map<String, Object> attrs = accessor.getSessionAttributes();
            Long userId = (Long) attrs.get("USER_ID");
            if (userId != null) {
                sessionManagerService.addSession(sessionId, userId);
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            if (sessionId != null) {
                sessionManagerService.removeSession(sessionId);
            }
        } else {
            Long userId = sessionManagerService.getUserId(sessionId);
            if (userId != null) {
                accessor.setHeader("userId", userId);
            }
        }

        return message;
    }

}
