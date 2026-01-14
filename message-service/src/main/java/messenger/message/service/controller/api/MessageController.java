package messenger.message.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.message.service.dto.request.SendMessageRequest;
import messenger.message.service.dto.response.MessageResponse;
import messenger.message.service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        log.info("Sending message attempt by user {} and chat {} type {}",
                senderId, request.chatId(), request.chatType());

        MessageResponse response = messageService.sendMessage(request, senderId);

        log.info("User with id {} sent message to chat {} successfully", senderId, request.chatId());

        return ResponseEntity.status(200).body(response);
    }

//    @GetMapping("/chat/{chatId}")
//    public ResponseEntity<?> getMessages(
//            @PathVariable @Valid Long chatId,
//            @RequestParam(defaultValue = "0") @Valid int page,
//            @RequestParam(defaultValue = "100") @Valid int size,
//            @RequestParam @Valid Long userId
//    ) {
//        log.info("Getting messages attempt for userId {} and chatId {}", userId, chatId);
//
//        chatServiceClient.validateUserIsChatMember(chatId, userId);
//        List<MessageResponse> responses = messageService.getChatMessages(chatId, page, size);
//
//        log.info("User with id {} got messages from chat {} successfully", userId, chatId);
//
//        return ResponseEntity.status(200).body(responses);
//    }
//
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
