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
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageGrpcClient messageGrpcClient;

    @MessageMapping("/chat.send")
    public MessageResult processSendMessage(
            @Payload @Valid SendMessageRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long senderId
    ) {
        return messageGrpcClient.sendMessage(request, senderId);
    }

    @MessageMapping("/chat.edit")
    public MessageResult processEditMessage(
            @Payload @Valid EditMessageRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long editorId
    ) {
        return messageGrpcClient.editMessage(request, editorId);
    }

    @MessageMapping("/chat.read")
    public MessageResult processMarkAsRead(
            @Payload @Valid MarkMessageAsReadRequest request,
            @Header("simpSessionId") String sessionId,
            @Header("userId") Long readerId
    ) {
        return messageGrpcClient.markAsRead(request, readerId);
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
