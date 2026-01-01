package messenger.group.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.entity.GroupChat;
import messenger.group.chat.service.domain.repository.GroupChatRepository;
import messenger.group.chat.service.dto.request.CreateGroupChatRequest;
import messenger.group.chat.service.dto.response.GroupChatResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final MemberAdditionService memberAdditionService;

    @Transactional
    public GroupChatResponse createGroupChat(Long creatorId, CreateGroupChatRequest request) {
        GroupChat newGroup = GroupChat.builder()
                .createdBy(creatorId)
                .name(request.name())
                .description(request.description())
                .avatarUrl(request.avatarUrl())
                .createdBy(creatorId)
                .build();

        GroupChat savedGroup = groupChatRepository.save(newGroup);

        memberAdditionService.addOwnerToGroup(savedGroup, creatorId);
        memberAdditionService.addMembersToNewGroup(savedGroup, request.userIds());

        return createGroupChatResponse(savedGroup);
    }

    private GroupChatResponse createGroupChatResponse(GroupChat group) {
        return GroupChatResponse.builder()
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
