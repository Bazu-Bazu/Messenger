package com.burkina.messenger.mapper;

import com.burkina.messenger.domain.entity.GroupChatMember;
import com.burkina.messenger.dto.response.GroupMemberResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMemberMapper {

    public List<GroupMemberResponse> toResponses(List<GroupChatMember> groupMembers) {
        return groupMembers.stream()
                .map(this::createGroupMemberResponse)
                .toList();
    }

    private GroupMemberResponse createGroupMemberResponse(GroupChatMember groupMember) {
        return GroupMemberResponse.builder()
                .id(groupMember.getId())
                .role(groupMember.getRole())
                .build();
    }
}
