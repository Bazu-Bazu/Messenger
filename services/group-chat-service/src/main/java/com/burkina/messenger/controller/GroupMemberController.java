package com.burkina.messenger.controller;

import com.burkina.messenger.domain.entity.GroupChatMember;
import com.burkina.messenger.mapper.GroupMemberMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.dto.request.AddNewMembersRequest;
import com.burkina.messenger.dto.request.RemoveMembersRequest;
import com.burkina.messenger.dto.request.SetRolesRequest;
import com.burkina.messenger.dto.response.GroupMemberResponse;
import com.burkina.messenger.service.GroupService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats/groups")
@RequiredArgsConstructor
public class GroupMemberController {

    private final GroupService groupChatService;
    private final GroupMemberMapper memberMapper;

    @PostMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> addNewMembers(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid AddNewMembersRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        List<GroupChatMember> members = groupChatService.addNewMembers(userId, groupId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberMapper.toResponses(members));
    }

    @DeleteMapping("/{groupId}/members")
    public ResponseEntity<Void> removeMembers(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        groupChatService.removeMembers(userId, groupId, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> getMembers(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId,
            Pageable pageable
    ) {
        Long userId = Long.valueOf(jwt.getSubject());

        List<GroupChatMember> members = groupChatService.getGroupMembers(userId, groupId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(memberMapper.toResponses(members));
    }

    @PatchMapping("/{groupId}/members")
    public ResponseEntity<?> setRoles(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid SetRolesRequest request)
    {
        Long userId = Long.valueOf(jwt.getSubject());

        List<GroupChatMember> members = groupChatService.setRoles(userId, groupId, request);

        return ResponseEntity.status(HttpStatus.OK).body(memberMapper.toResponses(members));
    }
}
