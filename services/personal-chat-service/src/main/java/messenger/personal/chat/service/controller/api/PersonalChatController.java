package messenger.personal.chat.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.dto.request.CreatePersonalChatRequest;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.service.PersonalChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats/personal")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PersonalChatController {

    private final PersonalChatService personalChatService;

    @PostMapping
    public ResponseEntity<?> getOrCreatePersonalChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long user1Id,
            @RequestBody @Valid CreatePersonalChatRequest request
    ) {
        PersonalChatResponse response = personalChatService.getOrCreatePersonalChat(user1Id, request);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllUserPersonalChats(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<PersonalChatResponse> responses = personalChatService.getAllUserPersonalChats(userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deletePersonalChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("chatId") @Valid Long chatId
    ) {
        personalChatService.deletePersonalChat(userId, chatId);

        return ResponseEntity.status(200).body(null);
    }

}
