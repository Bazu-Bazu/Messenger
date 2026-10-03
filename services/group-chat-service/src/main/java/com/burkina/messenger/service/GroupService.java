package com.burkina.messenger.service;

import com.burkina.messenger.domain.entity.GroupChatMember;
import com.burkina.messenger.dto.request.*;
import com.burkina.messenger.service.event.GroupChatPublisher;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.GroupChat;
import com.burkina.messenger.domain.repository.GroupRepository;
import com.burkina.messenger.exception.GroupNotFoundException;
import com.burkina.messenger.validator.GroupPermissionValidator;
import com.burkina.messenger.validator.GroupValidator;
import com.burkina.messenger.validator.UserValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserValidator userValidator;
    private final GroupValidator groupValidator;
    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final GroupChatPublisher groupChatPublisher;
    private final GroupPermissionValidator groupPermissionValidator;

    @Transactional
    public GroupChat createGroupChat(Long creatorId, CreateGroupRequest request) {
        List<Long> members = request.userIds();

        groupValidator.validateCreatorNotInMembers(creatorId, members);

        userValidator.validateUsersExist(members);

        GroupChat newGroup = GroupChat.builder()
                .createdBy(creatorId)
                .name(request.name())
                .avatarId(request.avatarId())
                .build();

        groupRepository.save(newGroup);

        groupMemberService.addOwner(newGroup, creatorId);
        groupMemberService.addMembers(newGroup, members);

        groupChatPublisher.publishGroupChatCreated(newGroup);

        return newGroup;
    }

    @Transactional
    public void deleteGroup(Long removerId, Long groupId) {
        GroupChat group = findGroupById(groupId);

        groupPermissionValidator.validateCanDeleteGroup(removerId, groupId);

        groupRepository.delete(group);

        groupChatPublisher.publishGroupChatDeleted(group);
    }

    @Transactional
    public List<GroupChatMember> addNewMembers(Long invitorId, Long groupId, AddNewMembersRequest request) {
        List<Long> members = request.userIds();

        GroupChat group = findGroupById(groupId);

        groupPermissionValidator.validateCanAddMembers(invitorId, groupId);

        groupValidator.validateMembersNotAlreadyInGroup(groupId, members);

        userValidator.validateUsersExist(members);

        List<GroupChatMember> newMembers = groupMemberService.addMembers(group, members);

        groupChatPublisher.publishGroupChatMembersAdded(groupId, newMembers);

        return newMembers;
    }

    @Transactional
    public void removeMembers(Long removerId, Long groupId, RemoveMembersRequest request) {
        List<Long> members = request.userIds();

        findGroupById(groupId);

        groupPermissionValidator.validateCanRemoveMembers(removerId, groupId, members);

        groupMemberService.removeMembers(groupId, members);

        groupChatPublisher.publishGroupChatMembersDeleted(groupId, members);
    }

    public List<GroupChatMember> getGroupMembers(Long userId, Long groupId, Pageable pageable) {
        findGroupById(groupId);

        groupPermissionValidator.validateCanGetGroupMembers(userId, groupId);

        return groupMemberService.getGroupMembers(groupId, pageable);
    }

    @Transactional
    public GroupChat changeGroupInfo(Long changerId, Long groupId, ChangeGroupInfoRequest request) {
        groupPermissionValidator.validateCanChangeGroupInfo(changerId, groupId);

        GroupChat group = findGroupById(groupId);

        group.changeFrom(request);

        groupChatPublisher.publishGroupChatInfoUpdated(group);

        return group;
    }

    @Transactional
    public List<GroupChatMember> setRoles(Long setterId, Long groupId, SetRolesRequest request) {
        List<Long> members = request.userIds();

        findGroupById(groupId);

        groupPermissionValidator.validateCanSetRole(setterId, groupId, members, request.role());

        List<GroupChatMember> updatedMembers = groupMemberService.setRoles(groupId, request.userIds(), request.role());

        groupChatPublisher.publishGroupChatMembersUpdated(groupId, updatedMembers);

        return updatedMembers;
    }

    public GroupChat getGroup(Long userId, Long groupId) {
        groupPermissionValidator.validateCanGetGroupInfo(userId, groupId);

        return findGroupById(groupId);
    }

    public GroupChat findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(
                        String.format("Group %d not found", groupId)
                ));
    }
}
