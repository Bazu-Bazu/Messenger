package messenger.message.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import dto.request.EditMessageRequest;
import dto.request.GetMessagesRequest;
import dto.request.MarkMessageAsReadRequest;
import dto.request.SendMessageRequest;
import dto.response.MessageResponse;
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

    @PatchMapping("/edit")
    public ResponseEntity<?> editMessage(
            @RequestParam("editorId") @Valid Long editorId,
            @RequestBody @Valid EditMessageRequest request
    ) {
        log.info("Editing message attempt by user {} and message {} in {} chat {}",
                editorId, request.messageId(), request.chatType(), request.chatId());

        MessageResponse response = messageService.editMessage(request, editorId);

        log.info("User {} edited message {} in {} chat {} successfully",
                editorId, request.messageId(), request.chatType(), request.chatId());

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/read")
    public ResponseEntity<?> markMessageAsRead(
            @RequestParam("readerId") @Valid Long readerId,
            @RequestBody @Valid MarkMessageAsReadRequest request
    ) {
        log.info("Mark message {} as read attempt bu user {}", request.messageId(), readerId);

        MessageResponse response = messageService.markMessageAsRead(readerId, request);

        log.info("Message {} marked as read by user {} successfully", request.messageId(), readerId);

        return ResponseEntity.status(200).body(response);
    }

}
