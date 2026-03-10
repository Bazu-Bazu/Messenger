package messenger.web.socket.service.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.ErrorResponse;
import dto.result.MessageResult;
import lombok.RequiredArgsConstructor;
import messenger.web.socket.service.client.grpc.MessageGrpcClient;
import messenger.web.socket.service.exception.AuthorizationException;
import messenger.web.socket.service.exception.mapper.ErrorMapper;
import messenger.web.socket.service.service.SessionManagerService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final SessionManagerService sessionManagerService;
    private final MessageGrpcClient messageGrpcClient;
    private final ObjectMapper objectMapper;
    private final ErrorMapper errorMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("USER_ID");
        if (userId != null) {
            sessionManagerService.addSession(session, userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("USER_ID");
        if (userId == null) {
            sendError(session, new AuthorizationException("Unauthorized"));
            return;
        }

        JsonNode root = objectMapper.readTree(message.getPayload());
        String action = root.path("action").asText(null);

        MessageResult result = switch (action) {
            case "send" -> handleSend(root, userId);
            case "read" -> handleRead(root, userId);
            default -> MessageResult.error(
                    ErrorResponse.builder()
                            .errorCode(400)
                            .error("UNKNOWN_ACTION")
                            .message("Unknown action: " + action)
                            .timestamp(Instant.now())
                            .build()
            );
        };

        session.sendMessage(
                new TextMessage(objectMapper.writeValueAsString(result))
        );
    }

    private MessageResult handleSend(JsonNode payload, Long userId) {
        SendMessageRequest request = objectMapper.convertValue(payload, SendMessageRequest.class);
        return messageGrpcClient.sendMessage(request, userId);
    }

    private MessageResult handleRead(JsonNode payload, Long userId) {
        MarkMessageAsReadRequest request = objectMapper.convertValue(payload, MarkMessageAsReadRequest.class);
        return messageGrpcClient.markAsRead(request, userId);
    }

    private void sendError(WebSocketSession session, Exception e) throws IOException {
        MessageResult error = MessageResult.error(errorMapper.from(e));
        session.sendMessage(
                new TextMessage(objectMapper.writeValueAsString(error))
        );
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManagerService.removeSession(session.getId());
    }
}
