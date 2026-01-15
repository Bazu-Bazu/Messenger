package messenger.message.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.message.service.dto.request.GetMessagesRequest;
import messenger.message.service.dto.request.SendMessageRequest;
import messenger.message.service.dto.response.MessageResponse;
import messenger.message.service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Log4j2
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam("senderId") @Valid Long senderId,
            @RequestBody @Valid SendMessageRequest request
    ) {
        log.info("Sending message attempt by user {} and {} chat {}",
                senderId, request.chatType(), request.chatId());

        MessageResponse response = messageService.sendMessage(request, senderId);

        log.info("User {} sent message to {} chat {}", senderId, request.chatType(), request.chatId());

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getMessages(
            @RequestParam("getterId") @Valid Long getterId,
            @RequestBody @Valid GetMessagesRequest request
    ) {
        log.info("Getting messages attempt by user {} and {} chat {}",
                getterId, request.chatType(), request.chatId());

        List<MessageResponse> responses = messageService.getChatMessages(getterId, request);

        log.info("User {} got messages from {} chat {} successfully",
                getterId, request.chatType(), request.chatId());

        return ResponseEntity.status(200).body(responses);
    }

//    @PatchMapping("/edit")
//    public ResponseEntity<?> editMessage(
//            @RequestParam @Valid Long userId,
//            @RequestBody @Valid EditMessageRequest request
//    ) {
//        log.info("Editing message attempt for userId {} and messageId {}", userId, request.messageId());
//
//        chatServiceClient.validateUserCanSendMessage(request.chatId(), userId);
//        MessageResponse response = messageService.editMessage(request, userId);
//
//
//
//        log.info("User with id {} edited message with id {} successfully", userId, request.messageId());
//
//        return ResponseEntity.status(200).body(response);
//    }
//
//    @PatchMapping("/read/{messageId}")
//    public ResponseEntity<?> markMessageAsRead(@PathVariable @Valid Long messageId) {
//        log.info("Mark message with id {} as read attempt", messageId);
//
//        messageService.markMessageAsRead(messageId);
//
//        log.info("Message with id {} marked as read successfully", messageId);
//
//        return ResponseEntity.status(200).body(null);
//    }

}
