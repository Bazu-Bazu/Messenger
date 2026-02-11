package messenger.group.chat.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.dto.request.*;
import messenger.group.chat.service.dto.response.GroupChatResponse;
import messenger.group.chat.service.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats/group")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GroupChatController {

    private final GroupChatService groupChatService;

    @PostMapping
    public ResponseEntity<?> createGroupChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long creatorId,
            @RequestBody @Valid CreateGroupChatRequest request
    ) {
        GroupChatResponse response = groupChatService.createGroupChat(creatorId, request);

        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<?> changeGroupInfo(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long changerId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid ChangeGroupInfoRequest request
    ) {
        GroupChatResponse response = groupChatService.changeGroupInfo(changerId, groupId, request);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllUserGroupChats(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<GroupChatResponse> responses = groupChatService.getAllUserGroupChat(userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroupChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long ownerId,
            @PathVariable("groupId") Long groupId
    ) {
        groupChatService.deleteGroup(ownerId, groupId);

        return ResponseEntity.status(200).body(null);
    }

}
