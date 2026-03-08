package messenger.group.chat.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.dto.request.*;
import messenger.group.chat.service.dto.response.GroupResponse;
import messenger.group.chat.service.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats/group")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupChatService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroupChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long creatorId,
            @RequestBody @Valid CreateGroupRequest request
    ) {
        GroupResponse response = groupChatService.createGroupChat(creatorId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupResponse> changeGroupInfo(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long changerId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid ChangeGroupInfoRequest request
    ) {
        GroupResponse response = groupChatService.changeGroupInfo(changerId, groupId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllUserGroupChats(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        List<GroupResponse> responses = groupChatService.getAllUserGroupChat(userId);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroupChat(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long ownerId,
            @PathVariable("groupId") Long groupId
    ) {
        groupChatService.deleteGroup(ownerId, groupId);

        return ResponseEntity.noContent().build();
    }
}
