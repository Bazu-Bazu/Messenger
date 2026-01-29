package messenger.personal.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.service.PersonalChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personal-chat")
@RequiredArgsConstructor
public class PersonalChatController {

    private final PersonalChatService personalChatService;

    @PostMapping("/get-or-create")
    public ResponseEntity<?> getOrCreatePersonalChat(
            @RequestParam("user1Id") @Valid Long user1Id,
            @RequestParam("user2Id") @Valid Long user2Id
    ) {
        PersonalChatResponse response = personalChatService.getOrCreatePersonalChat(user1Id, user2Id);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/get-all-user-personal-chats")
    public ResponseEntity<?> getAllUserPersonalChats(@RequestParam("userId") @Valid Long userId) {
        List<PersonalChatResponse> responses = personalChatService.getAllUserPersonalChats(userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePersonalChat(
            @RequestParam("userId") @Valid Long userId,
            @RequestParam("chatId") @Valid Long chatId
    ) {
        personalChatService.deletePersonalChat(userId, chatId);

        return ResponseEntity.status(200).body(null);
    }

}
