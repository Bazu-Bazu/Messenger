package com.burkina.messenger.mapper;

import com.burkina.common.dto.event.messenger.groupChat.*;
import com.burkina.common.dto.event.messenger.groupChat.common.GroupChatMemberInfo;
import com.burkina.messenger.domain.entity.GroupChat;
import com.burkina.messenger.domain.entity.GroupChatMember;
import com.burkina.messenger.dto.response.GroupResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    public GroupResponse toResponse(GroupChat chat) {
        return GroupResponse.builder()
                .id(chat.getId())
                .name(chat.getName())
                .createdBy(chat.getCreatedBy())
                .createdAt(chat.getCreatedAt())
                .avatarId(chat.getAvatarId())
                .build();
    }

    public GroupChatCreatedEvent toGroupChatCreatedEvent(GroupChat chat) {
        return GroupChatCreatedEvent.builder()
                .chatId(chat.getId())
                .name(chat.getName())
                .avatarId(chat.getAvatarId())
                .userIds(chat.getMembers().stream()
                        .map(GroupChatMember::getUserId)
                        .toList())
                .build();
    }

    public GroupChatDeletedEvent toGroupChatDeletedEvent(GroupChat chat) {
        return GroupChatDeletedEvent.builder()
                .chatId(chat.getId())
                .build();
    }

    public GroupChatInfoUpdatedEvent toGroupChatInfoUpdatedEvent(GroupChat chat) {
        return GroupChatInfoUpdatedEvent.builder()
                .chatId(chat.getId())
                .name(chat.getName())
                .avatarId(chat.getAvatarId())
                .build();
    }

    public GroupChatMembersAddedEvent toGroupChatMembersAddedEvent(Long groupId, List<GroupChatMember> members) {
        return GroupChatMembersAddedEvent.builder()
                .chatId(groupId)
                .userIds(members.stream()
                        .map(GroupChatMember::getUserId)
                        .toList())
                .build();
    }

    public GroupChatMembersUpdatedEvent toGroupChatMembersUpdatedEvent(Long groupId, List<GroupChatMember> members) {
        return GroupChatMembersUpdatedEvent.builder()
                .chatId(groupId)
                .members(members.stream()
                        .map(member -> new GroupChatMemberInfo(
                                member.getUserId(),
                                member.canSendMessage()))
                        .toList())
                .build();
    }

    public GroupChatMembersDeletedEvent toGroupChatMembersDeletedEvent(Long groupId, List<Long> userIds) {
        return GroupChatMembersDeletedEvent.builder()
                .chatId(groupId)
                .userIds(userIds)
                .build();
    }
}
