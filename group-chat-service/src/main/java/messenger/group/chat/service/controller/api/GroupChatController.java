package messenger.group.chat.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.group.chat.service.dto.request.AddNewMembersRequest;
import messenger.group.chat.service.dto.request.CreateGroupChatRequest;
import messenger.group.chat.service.dto.request.RemoveMembersRequest;
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

        GroupChatResponse response = groupChatService.addNewMembers(invitorId, request);

        log.info("New members added to group {} by user {} successfully", request.groupId(), invitorId);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/remove-members")
    public ResponseEntity<?> removeMembers(
            @RequestParam @Valid Long removerId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        log.info("Removing members from group {} attempt by user {}", request.groupId(), removerId);

        GroupChatResponse response = groupChatService.removeMembers(removerId, request);

        log.info("Members removed from group {} by user {} successfully", request.groupId(), removerId);

        return ResponseEntity.status(200).body(response);
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
