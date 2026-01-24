package messenger.message.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam("senderId") @Valid Long senderId,
            @RequestBody @Valid SendMessageRequest request
    ) {
        MessageResponse response = messageService.sendMessage(request, senderId);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getMessages(
            @RequestParam("getterId") @Valid Long getterId,
            @RequestBody @Valid GetMessagesRequest request
    ) {
        List<MessageResponse> responses = messageService.getChatMessages(getterId, request);

        return ResponseEntity.status(200).body(responses);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editMessage(
            @RequestParam("editorId") @Valid Long editorId,
            @RequestBody @Valid EditMessageRequest request
    ) {
        MessageResponse response = messageService.editMessage(request, editorId);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/read")
    public ResponseEntity<?> markMessageAsRead(
            @RequestParam("readerId") @Valid Long readerId,
            @RequestBody @Valid MarkMessageAsReadRequest request
    ) {
        MessageResponse response = messageService.markMessageAsRead(readerId, request);

        return ResponseEntity.status(200).body(response);
    }

}
