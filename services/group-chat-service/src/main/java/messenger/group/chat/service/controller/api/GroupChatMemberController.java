package messenger.group.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.dto.request.AddNewMembersRequest;
import messenger.group.chat.service.dto.request.RemoveMembersRequest;
import messenger.group.chat.service.dto.request.SetRolesRequest;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats/group/member")
@RequiredArgsConstructor
public class GroupChatMemberController {

    private final GroupChatService groupChatService;

    @PostMapping("/{groupId}")
    public ResponseEntity<?> addNewMembers(
            @RequestHeader("X-User-Id") Long invitorId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid AddNewMembersRequest request
    ) {
        groupChatService.addNewMembers(invitorId, groupId, request);

        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> removeMembers(
            @RequestHeader("X-User-Id") Long removerId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        groupChatService.removeMembers(removerId, groupId, request);

        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getMembers(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("groupId") Long groupId
    ) {
        List<GroupMemberResponse> responses = groupChatService.getGroupMembers(userId, groupId);

        return ResponseEntity.status(200).body(responses);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<?> setRoles(
            @RequestHeader("X-User-Id") Long setterId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid SetRolesRequest request)
    {
        List<GroupMemberResponse> responses = groupChatService.setRoles(setterId, groupId, request);

        return ResponseEntity.status(200).body(responses);
    }

}
