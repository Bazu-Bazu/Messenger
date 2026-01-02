package messenger.group.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.group.chat.service.dto.request.AddNewMembersRequest;
import messenger.group.chat.service.dto.request.CreateGroupChatRequest;
import messenger.group.chat.service.dto.request.RemoveMembersRequest;
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
            @RequestParam @Valid Long creatorId,
            @RequestBody @Valid CreateGroupChatRequest request
    ) {
        log.info("Creating group chat attempt by user {}", creatorId);

        GroupChatResponse response = groupChatService.createGroupChat(creatorId, request);

        log.info("Group chat {} created by user {} successfully", response.id(), creatorId);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/add-members")
    public ResponseEntity<?> addNewMembers(
            @RequestParam @Valid Long invitorId,
            @RequestBody @Valid AddNewMembersRequest request
    ) {
        log.info("Adding new members to group {} attempt by user {}", request.groupId(), invitorId);

        groupChatService.addNewMembers(invitorId, request);

        log.info("New members added to group {} by user {} successfully", request.groupId(), invitorId);

        return ResponseEntity.status(200).body(null);
    }

    @DeleteMapping("/remove-members")
    public ResponseEntity<?> removeMembers(
            @RequestParam @Valid Long removerId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        log.info("Removing members from group {} attempt by user {}", request.groupId(), removerId);

        groupChatService.removeMembers(removerId, request);

        log.info("Members removed from group {} by user {} successfully", request.groupId(), removerId);

        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/get-members")
    public ResponseEntity<?> getMembers(
            @RequestParam @Valid Long userId,
            @RequestParam @Valid Long groupId
    ) {
        log.info("Getting group {} members by attempt by user {}", groupId, userId);

        List<GroupMemberResponse> responses = groupChatService.getGroupMembers(userId, groupId);

        log.info("Group {} members got by user {} successfully", groupId, userId);

        return ResponseEntity.status(200).body(responses);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGroupChat(
            @RequestParam @Valid Long ownerId,
            @RequestParam @Valid Long groupId
    ) {
        log.info("Deleting group chat {} attempt by user {}", groupId, ownerId);

        groupChatService.deleteGroup(ownerId, groupId);

        log.info("Group chat {} deleted by user {} successfully", groupId, ownerId);

        return ResponseEntity.status(200).body(null);
    }

}
