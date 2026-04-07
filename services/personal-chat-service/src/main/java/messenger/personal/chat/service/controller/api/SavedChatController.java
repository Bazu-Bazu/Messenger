package messenger.personal.chat.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.dto.response.SavedChatResponse;
import messenger.personal.chat.service.service.SavedChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats/saved")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SavedChatController {

    private final SavedChatService savedChatService;

    @PostMapping
    public ResponseEntity<SavedChatResponse> getOrCreateSavedChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        SavedChatResponse response = savedChatService.getOrCreate(userId);

        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSavedChat(
        @Parameter(hidden = true)
        @RequestHeader("X-User-Id") Long userId
    ) {
        savedChatService.delete(userId);

        return ResponseEntity.noContent().build();
    }
}
