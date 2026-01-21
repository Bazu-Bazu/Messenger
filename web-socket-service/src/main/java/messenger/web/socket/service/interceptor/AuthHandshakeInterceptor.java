package messenger.web.socket.service.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@Log4j2
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String userIdParam = httpRequest.getParameter("userId");
            if (userIdParam != null) {
                try {
                    Long userId = Long.parseLong(userIdParam);
                    attributes.put("USER_ID", userId);
                    log.info("UserId from query param: {}", userId);
                    return true;
                } catch (NumberFormatException e) {
                    log.error("Invalid userId in query: {}", userIdParam);
                }
            }

            String userIdHeader = httpRequest.getHeader("X-User-Id");
            if (userIdHeader != null) {
                try {
                    Long userId = Long.parseLong(userIdHeader);
                    attributes.put("USER_ID", userId);
                    log.info("UserId from header: {}", userId);
                    return true;
                } catch (NumberFormatException e) {
                    log.error("Invalid userId in header: {}", userIdHeader);
                }
            }
        }

        log.warn("⚠️ No userId provided in handshake");
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        if (exception != null) {
            log.error("Handshake error: ", exception);
        } else {
            log.info("Handshake completed successfully");
        }
    }

}
