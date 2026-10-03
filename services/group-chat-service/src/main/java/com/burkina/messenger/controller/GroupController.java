package com.burkina.messenger.controller;

import com.burkina.messenger.domain.entity.GroupChat;
import com.burkina.messenger.dto.request.ChangeGroupInfoRequest;
import com.burkina.messenger.dto.request.CreateGroupRequest;
import com.burkina.messenger.mapper.GroupMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.dto.response.GroupResponse;
import com.burkina.messenger.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupMapper chatMapper;
    private final GroupService groupChatService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroupChat(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateGroupRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        GroupChat chat = groupChatService.createGroupChat(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(chatMapper.toResponse(chat));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroupChat(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        GroupChat chat = groupChatService.getGroup(userId, groupId);

        return ResponseEntity.status(HttpStatus.OK).body(chatMapper.toResponse(chat));
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<GroupResponse> changeGroupInfo(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid ChangeGroupInfoRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        GroupChat chat = groupChatService.changeGroupInfo(userId, groupId, request);

        return ResponseEntity.status(HttpStatus.OK).body(chatMapper.toResponse(chat));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroupChat(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        groupChatService.deleteGroup(userId, groupId);

        return ResponseEntity.noContent().build();
    }
}
