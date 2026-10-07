package com.burkina.messenger.service;

import com.burkina.common.dto.event.messenger.groupChat.*;
import com.burkina.common.enums.messenger.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.burkina.messenger.domain.entity.Chat;
import com.burkina.messenger.domain.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class GroupChatService {

    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final UserChatService userChatService;

    @Transactional
    public void create(GroupChatCreatedEvent event) {
        Chat chat = Chat.builder()
                .chatId(event.chatId())
                .chatType(ChatType.GROUP)
                .name(event.name())
                .avatarId(event.avatarId())
                .build();

        Chat savedChat = chatRepository.save(chat);

        userChatService.createUsersChat(savedChat, event.userIds());
    }

    @Transactional
    public void addMembers(GroupChatMembersAddedEvent event) {
        Chat chat = chatService.findChatByIdAndType(event.chatId(), ChatType.GROUP);

        if (event.userIds() != null) {
            userChatService.createUsersChat(chat, event.userIds());
        } else {
            log.warn("Attempt add members without members in {} chat {}", ChatType.GROUP, event.chatId());
        }
    }

    @Transactional
    public void changePermissions(GroupChatMembersUpdatedEvent event) {
        if (event.members() != null) {
            userChatService.changeRoles(event.chatId(), event.members());
        } else {
            log.warn("Attempt change roles without members in {} chat {}", ChatType.GROUP, event.chatId());
        }
    }

    @Transactional
    public void removeMembers(GroupChatMembersDeletedEvent event) {
        if (event.userIds() != null) {
            userChatService.deleteUsersChat(event.chatId(), event.userIds());
        } else {
            log.warn("Attempt remove members without members in {} chat {}", ChatType.GROUP, event.chatId());
        }
    }

    @Transactional
    public void changeInfo(GroupChatInfoUpdatedEvent event) {
        int updated = chatRepository.changeInfoForGroup(event.chatId(), ChatType.GROUP, event.name(), event.avatarId());

        if (updated == 0) {
            log.warn("{} chat {} info not changed", ChatType.GROUP, event.chatId());
        }
    }

    @Transactional
    public void delete(GroupChatDeletedEvent event) {
        int updated = chatRepository.deleteByChatIdAndChatType(event.chatId(), ChatType.GROUP);

        if (updated == 0) {
            log.warn("{} chat {} already deleted or not found", ChatType.PERSONAL, event.chatId());
        }
    }
}
