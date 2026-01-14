package messenger.group.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class GroupChatController {

    private final GroupChatService groupChatService;

    @PostMapping("/create")
    public ResponseEntity<?> createGroupChat(
            @RequestParam("creatorId") @Valid Long creatorId,
            @RequestBody @Valid CreateGroupChatRequest request
    ) {
        log.info("Creating group chat attempt by user {}", creatorId);

        GroupChatResponse response = groupChatService.createGroupChat(creatorId, request);

        log.info("Group chat {} created by user {} successfully", response.id(), creatorId);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/add-members")
    public ResponseEntity<?> addNewMembers(
            @RequestParam("invitorId") @Valid Long invitorId,
            @RequestBody @Valid AddNewMembersRequest request
    ) {
        log.info("Adding new members to group {} attempt by user {}", request.groupId(), invitorId);

        groupChatService.addNewMembers(invitorId, request);

        log.info("New members added to group {} by user {} successfully", request.groupId(), invitorId);

        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/remove-members")
    public ResponseEntity<?> removeMembers(
            @RequestParam("removerId") @Valid Long removerId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        log.info("Removing members from group {} attempt by user {}", request.groupId(), removerId);

        groupChatService.removeMembers(removerId, request);

        log.info("Members removed from group {} by user {} successfully", request.groupId(), removerId);

        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/get-members")
    public ResponseEntity<?> getMembers(
            @RequestParam("userId") @Valid Long userId,
            @RequestParam("groupId") @Valid Long groupId
    ) {
        log.info("Getting group {} members by attempt by user {}", groupId, userId);

        List<GroupMemberResponse> responses = groupChatService.getGroupMembers(userId, groupId);

        log.info("Group {} members got by user {} successfully", groupId, userId);

        return ResponseEntity.status(200).body(responses);
    }

    @PatchMapping("/change-info")
    public ResponseEntity<?> changeGroupInfo(
            @RequestParam("changerId") @Valid Long changerId,
            @RequestBody @Valid ChangeGroupInfoRequest request
    ) {
        log.info("Changing group {} info by user {}", request.groupId(), changerId);

        GroupChatResponse response = groupChatService.changeGroupInfo(changerId, request);

        log.info("Group {} info changed by user {} successfully", request.groupId(), changerId);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/get-all-user-group-chats")
    public ResponseEntity<?> getAllUserGroupChats(@RequestParam("userId") @Valid Long userId) {
        log.info("Getting all user {} group chats attempt", userId);

        List<GroupChatResponse> responses = groupChatService.getAllUserGroupChat(userId);

        log.info("User {} got all your own group chats", userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGroupChat(
            @RequestParam("ownerId") @Valid Long ownerId,
            @RequestParam("groupId") @Valid Long groupId
    ) {
        log.info("Deleting group chat {} attempt by user {}", groupId, ownerId);

        groupChatService.deleteGroup(ownerId, groupId);

        log.info("Group chat {} deleted by user {} successfully", groupId, ownerId);

        return ResponseEntity.status(200).body(null);
    }

    @PatchMapping("/set-roles")
    public ResponseEntity<?> setRoles(
            @RequestParam("setterId") @Valid Long setterId,
            @RequestBody @Valid SetRolesRequest request)
    {
        log.info("Setting role in group {} by user {}", request.groupId(), setterId);

        List<GroupMemberResponse> responses = groupChatService.setRoles(setterId, request);

        log.info("New roles in group {} by user {} set successfully", request.groupId(), setterId);

        return ResponseEntity.status(200).body(responses);
    }

}
