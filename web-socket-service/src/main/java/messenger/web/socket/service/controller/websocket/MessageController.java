package messenger.web.socket.service.controller.websocket;

import dto.request.EditMessageRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.response.ErrorResponse;
import dto.response.MessageResponse;
import dto.result.MessageResult;
import dto.request.SendMessageRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.web.socket.service.client.grpc.MessageGrpcClient;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
@Log4j2
public class MessageController {

    private final MessageGrpcClient messageGrpcClient;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.send")
    public MessageResult processSendMessage(
            @Payload @Valid SendMessageRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long senderId
    ) {
        log.info("Sending message attempt: userId={}, chatType={}, chatId={}, session={}",
                senderId, request.chatType(), request.chatId(), sessionId);

        MessageResult result = messageGrpcClient.sendMessage(request, senderId);

        if (result.isSuccess()) {
            String destination = String.format("/topic/chat.%s.%s",
                    request.chatType().toString().toLowerCase(),
                    request.chatId());

            messagingTemplate.convertAndSend(destination, result);

            MessageResponse success = result.success();
            log.info("Message sent successfully: messageId={}, userId={}, chatType={}, chatId={}, session={}",
                    success.id(), senderId, success.chatType(), success.chatId(), sessionId);
        } else {
            log.warn("Failed to sent message: userId={}, chatType={}, chatId={}, session={}, error={}",
                    senderId, request.chatType(), request.chatId(), sessionId, result.error().getError());
        }

        return result;
    }

    @MessageMapping("/chat.edit")
    public MessageResult processEditMessage(
            @Payload @Valid EditMessageRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long editorId
    ) {
        log.info("Edit message attempt: messageId={}, userId={}, chatType={}, chatId={}, session={}",
                request.messageId(), editorId, request.chatType(), request.chatId(), sessionId);

        MessageResult result = messageGrpcClient.editMessage(request, editorId);

        if (result.isSuccess()) {
            String destination = String.format("/topic/chat.%s.%s",
                    request.chatType().toString().toLowerCase(),
                    request.chatId());

            messagingTemplate.convertAndSend(destination, result);

            MessageResponse success = result.success();
            log.info("Message edited successfully: messageId={}, userId={}, chatType={}, chatId={}, session={}",
                    success.id(), editorId, success.chatType(), success.chatId(), sessionId);
        } else {
            log.warn("Failed to edit message: messageId={}, userId={}, chatType={}, chatId={}, session={}, " +
                            "error={}",
                    request.messageId(), editorId, request.chatType(), request.chatId(), sessionId,
                    result.error().getError());
        }

        return result;
    }

    @MessageMapping("/chat.read")
    public MessageResult processMarkAsRead(
            @Payload @Valid MarkMessageAsReadRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long readerId
    ) {
        log.info("Read message attempt: messageId={}, userId={}, chatType={}, chatId={}, session={}",
                request.messageId(), readerId, request.chatType(), request.chatId(), sessionId);

        MessageResult result = messageGrpcClient.markAsRead(request, readerId);

        if (result.isSuccess()) {
            String destination = String.format("/topic/chat.%s.%s",
                    request.chatType().toString().toLowerCase(),
                    request.chatId());

            messagingTemplate.convertAndSend(destination, result);

            MessageResponse success = result.success();
            log.info("Message read successfully: messageId={}, userId={}, chatType={}, chatId={}, session={}",
                    success.id(), readerId, success.chatType(), success.chatId(), sessionId);
        } else {
            log.warn("Failed to read message: messageId={}, userId={}, chatType={}, chatId={}, session={}, " +
                            "error={}",
                    request.messageId(), readerId, request.chatType(), request.chatId(), sessionId,
                    result.error().getError());
        }

        return result;
    }

    @MessageExceptionHandler
    @SendTo("/topic/errors")
    public ErrorResponse handleException(
            Exception ex,
            @Header(value = "simpSessionId", required = false) String sessionId
    ) {
        String session = sessionId != null ? sessionId : "unknown";

        log.error("Controller error [session={}]: {}", session, ex.getMessage(), ex);

        return ErrorResponse.builder()
                .errorCode(500)
                .error("CONTROLLER_ERROR")
                .message("Failed to process message")
                .timestamp(Instant.now())
                .build();
    }

}
