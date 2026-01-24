package messenger.web.socket.service.controller.websocket;

import dto.request.EditMessageRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.response.ErrorResponse;
import dto.result.MessageResult;
import dto.request.SendMessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.web.socket.service.client.grpc.MessageGrpcClient;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageGrpcClient messageGrpcClient;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.send")
    public MessageResult processSendMessage(
            @Payload @Valid SendMessageRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long senderId
    ) {
        MessageResult result = messageGrpcClient.sendMessage(request, senderId);

        if (result.isSuccess()) {
            String destination = String.format("/topic/chat.%s.%s",
                    request.chatType().toString().toLowerCase(),
                    request.chatId());

            messagingTemplate.convertAndSend(destination, result);
        }

        return result;
    }

    @MessageMapping("/chat.edit")
    public MessageResult processEditMessage(
            @Payload @Valid EditMessageRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long editorId
    ) {
        MessageResult result = messageGrpcClient.editMessage(request, editorId);

        if (result.isSuccess()) {
            String destination = String.format("/topic/chat.%s.%s",
                    request.chatType().toString().toLowerCase(),
                    request.chatId());

            messagingTemplate.convertAndSend(destination, result);
        }

        return result;
    }

    @MessageMapping("/chat.read")
    public MessageResult processMarkAsRead(
            @Payload @Valid MarkMessageAsReadRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long readerId
    ) {
        MessageResult result = messageGrpcClient.markAsRead(request, readerId);

        if (result.isSuccess()) {
            String destination = String.format("/topic/chat.%s.%s",
                    request.chatType().toString().toLowerCase(),
                    request.chatId());

            messagingTemplate.convertAndSend(destination, result);
        }

        return result;
    }

    @MessageExceptionHandler
    @SendTo("/topic/errors")
    public ErrorResponse handleException(
            Exception ex,
            @Header(value = "simpSessionId", required = false) String sessionId
    ) {
        return ErrorResponse.builder()
                .errorCode(500)
                .error("CONTROLLER_ERROR")
                .message("Failed to process message")
                .timestamp(Instant.now())
                .build();
    }

}
