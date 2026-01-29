package messenger.group.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.dto.request.*;
import messenger.group.chat.service.dto.response.GroupChatResponse;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group-chat")
@RequiredArgsConstructor
public class GroupChatController {

    private final GroupChatService groupChatService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroupChat(
            @RequestParam("creatorId") @Valid Long creatorId,
            @RequestBody @Valid CreateGroupChatRequest request
    ) {
        GroupChatResponse response = groupChatService.createGroupChat(creatorId, request);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/add-members")
    public ResponseEntity<?> addNewMembers(
            @RequestParam("invitorId") @Valid Long invitorId,
            @RequestBody @Valid AddNewMembersRequest request
    ) {
        groupChatService.addNewMembers(invitorId, request);

        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/remove-members")
    public ResponseEntity<?> removeMembers(
            @RequestParam("removerId") @Valid Long removerId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        groupChatService.removeMembers(removerId, request);

        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/get-members")
    public ResponseEntity<?> getMembers(
            @RequestParam("userId") @Valid Long userId,
            @RequestParam("groupId") @Valid Long groupId
    ) {
        List<GroupMemberResponse> responses = groupChatService.getGroupMembers(userId, groupId);

        return ResponseEntity.status(200).body(responses);
    }

    @PatchMapping("/change-info")
    public ResponseEntity<?> changeGroupInfo(
            @RequestParam("changerId") @Valid Long changerId,
            @RequestBody @Valid ChangeGroupInfoRequest request
    ) {
        GroupChatResponse response = groupChatService.changeGroupInfo(changerId, request);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/get-all-user-group-chats")
    public ResponseEntity<?> getAllUserGroupChats(@RequestParam("userId") @Valid Long userId) {
        List<GroupChatResponse> responses = groupChatService.getAllUserGroupChat(userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGroupChat(
            @RequestParam("ownerId") @Valid Long ownerId,
            @RequestParam("groupId") @Valid Long groupId
    ) {
        groupChatService.deleteGroup(ownerId, groupId);

        return ResponseEntity.status(200).body(null);
    }

    @PatchMapping("/set-roles")
    public ResponseEntity<?> setRoles(
            @RequestParam("setterId") @Valid Long setterId,
            @RequestBody @Valid SetRolesRequest request)
    {
        List<GroupMemberResponse> responses = groupChatService.setRoles(setterId, request);

        return ResponseEntity.status(200).body(responses);
    }

}
