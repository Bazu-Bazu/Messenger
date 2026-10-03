package com.burkina.messenger.service;

import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.GroupChat;
import com.burkina.messenger.domain.entity.GroupChatMember;
import com.burkina.messenger.domain.enums.GroupMemberRole;
import com.burkina.messenger.domain.repository.GroupMemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public void addOwner(GroupChat group, Long ownerId) {
        group.addMember(createGroupMember(ownerId, GroupMemberRole.OWNER));
    }

    @Transactional
    public List<GroupChatMember> addMembers(GroupChat group, List<Long> userIds) {
        List<GroupChatMember> newMembers = userIds.stream()
                .map(userId -> (createGroupMember(userId, GroupMemberRole.MEMBER)))
                .toList();

        group.addMembers(newMembers);

        return groupMemberRepository.saveAll(newMembers);
    }

    @Transactional(readOnly = true)
    public List<GroupChatMember> getGroupMembers(Long groupId, Pageable pageable) {
        return groupMemberRepository.findAllByGroupId(groupId, pageable);
    }

    @Transactional
    public void removeMembers(Long groupId, List<Long> userIds) {
        groupMemberRepository.deleteByUserIdsAndGroupId(userIds, groupId);
    }

    @Transactional
    public List<GroupChatMember> setRoles(Long groupId, List<Long> userIds, GroupMemberRole role) {
        groupMemberRepository.setRoleByUserIdsAndGroupId(role, userIds, groupId);

        return groupMemberRepository.findAllByUserIdsAndGroupId(userIds, groupId);
    }

    private GroupChatMember createGroupMember(Long userId, GroupMemberRole role) {
        return GroupChatMember.builder()
                .userId(userId)
                .role(role)
                .build();
    }
}
