package messenger.web.socket.service.config;

import lombok.RequiredArgsConstructor;
import messenger.web.socket.service.handler.ChatWebSocketHandler;
import messenger.web.socket.service.interceptor.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableScheduling
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final AuthHandshakeInterceptor authHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }
}
