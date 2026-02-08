package messenger.web.socket.service.controller.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.event.MessageDetailEvent;
import dto.response.MessageResponse;
import dto.result.MessageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.web.socket.service.service.SessionManagerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class MessageEventConsumer {

    private final SessionManagerService sessionManagerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "message_sending")
    public void handleMessageEvent(MessageDetailEvent event) throws IOException {
        MessageResponse response = convertToMessageResponse(event);
        MessageResult result = MessageResult.success(response);

        String json = objectMapper.writeValueAsString(result);

        Set<WebSocketSession> sessions = sessionManagerService.getSessionsByUsersId(event.memberIds());

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                log.info("Sending message to session {}", session.getId());
                session.sendMessage(new TextMessage(json));
            }
        }
    }

    private MessageResponse convertToMessageResponse(MessageDetailEvent event) {
        return MessageResponse.builder()
                .id(event.id())
                .chatId(event.chatId())
                .chatType(event.chatType())
                .content(event.content())
                .senderId(event.senderId())
                .messageType(event.messageType())
                .createdAt(event.createdAt())
                .editedAt(event.editedAt())
                .readAt(event.readAt())
                .build();
    }

}
