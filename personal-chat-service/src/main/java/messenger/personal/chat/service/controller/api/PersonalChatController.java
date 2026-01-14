package messenger.personal.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.service.PersonalChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personal-chat")
@RequiredArgsConstructor
@Log4j2
public class PersonalChatController {

    private final PersonalChatService personalChatService;

    @PostMapping("/get-or-create")
    public ResponseEntity<?> getOrCreatePersonalChat(
            @RequestParam("user1Id") @Valid Long user1Id,
            @RequestParam("user2Id") @Valid Long user2Id
    ) {
        log.info("Getting or creating personal chat attempt between users {} and {}", user1Id, user2Id);

        PersonalChatResponse response = personalChatService.getOrCreatePersonalChat(user1Id, user2Id);

        log.info("Personal chat {} between users {} and {} got or created successfully",
                response.id(), user1Id, user2Id);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/get-all-user-personal-chats")
    public ResponseEntity<?> getAllUserPersonalChats(@RequestParam("userId") @Valid Long userId) {
        log.info("Getting all user {} personal chats attempt", userId);

        List<PersonalChatResponse> responses = personalChatService.getAllUserPersonalChats(userId);

        log.info("User {} got all your own personal chats", userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePersonalChat(
            @RequestParam("userId") @Valid Long userId,
            @RequestParam("chatId") @Valid Long chatId
    ) {
        log.info("Deleting chat {} by user {} attempt", chatId, userId);

        personalChatService.deletePersonalChat(userId, chatId);

        log.info("User {} deleted chat {} successfully", userId, chatId);

        return ResponseEntity.status(200).body(null);
    }

}
