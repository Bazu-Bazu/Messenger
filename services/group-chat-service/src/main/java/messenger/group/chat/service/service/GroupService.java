package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.domain.repository.GroupRepository;
import messenger.group.chat.service.dto.request.*;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.dto.response.GroupResponse;
import messenger.group.chat.service.exception.GroupNotFoundException;
import messenger.group.chat.service.validator.GroupPermissionValidator;
import messenger.group.chat.service.validator.GroupValidator;
import messenger.group.chat.service.validator.UserValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final UserValidator userValidator;
    private final GroupValidator groupValidator;
    private final GroupPermissionValidator groupPermissionValidator;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public GroupResponse createGroupChat(Long creatorId, CreateGroupRequest request) {
        List<Long> members = request.userIds();

        groupValidator.validateCreatorNotInMembers(creatorId, members);

        userValidator.validateUsersExist(members);

        GroupChat newGroup = GroupChat.builder()
                .createdBy(creatorId)
                .name(request.name())
                .description(request.description())
                .avatarUrl(request.avatarUrl())
                .build();

        groupRepository.save(newGroup);

        groupMemberService.addOwner(newGroup, creatorId);
        groupMemberService.addMembers(newGroup, members);

        return createGroupChatResponse(newGroup);
    }

    @Transactional
    public void deleteGroup(Long removerId, Long groupId) {
        GroupChat group = findGroupById(groupId);

        groupPermissionValidator.validateCanDeleteGroup(removerId, groupId);

        groupRepository.delete(group);
    }

    @Transactional
    public List<GroupMemberResponse> addNewMembers(Long invitorId, Long groupId, AddNewMembersRequest request) {
        List<Long> members = request.userIds();

        GroupChat group = findGroupById(groupId);

        groupPermissionValidator.validateCanAddMembers(invitorId, groupId);

        groupValidator.validateMembersNotAlreadyInGroup(groupId, members);

        userValidator.validateUsersExist(members);

        return groupMemberService.addMembers(group, members);
    }

    @Transactional
    public void removeMembers(Long removerId, Long groupId, RemoveMembersRequest request) {
        List<Long> members = request.userIds();

        findGroupById(groupId);

        groupPermissionValidator.validateCanRemoveMembers(removerId, groupId, members);

        groupMemberService.removeMembers(groupId, members);
    }

    public List<GroupMemberResponse> getGroupMembers(Long userId, Long groupId, Pageable pageable) {
        findGroupById(groupId);

        groupPermissionValidator.validateCanGetGroupMembers(userId, groupId);

        return groupMemberService.getGroupMembers(groupId, pageable);
    }

    @Transactional
    public GroupResponse changeGroupInfo(Long changerId, Long groupId, ChangeGroupInfoRequest request) {
        groupPermissionValidator.validateCanChangeGroupInfo(changerId, groupId);

        GroupChat group = findGroupById(groupId);

        group.changeFrom(request);
        GroupChat savedGroup = groupRepository.save(group);

        return createGroupChatResponse(savedGroup);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = GroupCacheService.USER_GROUP_CHATS_CACHE, key = "#p0")
    public List<GroupResponse> getAllUserGroupChat(Long userId) {
        List<GroupChat> groupIds = groupRepository.findAllUserChatIds(userId);

        return groupIds.stream()
                .map(this::createGroupChatResponse)
                .toList();
    }

    @Transactional
    public List<GroupMemberResponse> setRoles(Long setterId, Long groupId, SetRolesRequest request) {
        List<Long> members = request.userIds();

        findGroupById(groupId);

        groupPermissionValidator.validateCanSetRole(setterId, groupId, members, request.role());

        return groupMemberService.setRoles(groupId, request.userIds(), request.role());
    }

    @Transactional
    public void updateLastActivity(Long groupId, Instant lastActivity) {
        groupRepository.updateLastActivity(groupId, lastActivity);
    }

    public GroupChat findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(
                        String.format("Group %d not found", groupId)
                ));
    }

    @Transactional(readOnly = true)
    public Set<Long> getAllMembers(Long groupId) {
        return groupMemberRepository.findAllUserIdsByGroupId(groupId);
    }

    private GroupResponse createGroupChatResponse(GroupChat group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .createdBy(group.getCreatedBy())
                .createdAt(group.getCreatedAt())
                .lastActivityAt(group.getLastActivityAt())
                .avatarUrl(group.getAvatarUrl())
                .build();
    }
}
