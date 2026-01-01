package messenger.group.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.group.chat.service.dto.request.CreateGroupChatRequest;
import messenger.group.chat.service.dto.response.GroupChatResponse;
import messenger.group.chat.service.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group-chat")
@RequiredArgsConstructor
@Log4j2
public class GroupChatController {

    private final GroupChatService groupChatService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroupChat(
            @RequestParam @Valid Long creatorId,
            @RequestBody @Valid CreateGroupChatRequest request
    ) {
        log.info("Create group chat attempt by user {}", creatorId);

        GroupChatResponse response = groupChatService.createGroupChat(creatorId, request);

        log.info("Group chat {} created by user {} successfully", response.id(), creatorId);

        return ResponseEntity.status(201).body(response);
    }

}
