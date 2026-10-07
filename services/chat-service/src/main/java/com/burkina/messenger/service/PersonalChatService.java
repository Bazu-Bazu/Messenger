package com.burkina.messenger.service;

import com.burkina.common.dto.event.messenger.personalChat.PersonalChatCreatedEvent;
import com.burkina.common.dto.event.messenger.personalChat.PersonalChatDeletedEvent;
import com.burkina.common.enums.messenger.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.burkina.messenger.domain.entity.Chat;
import com.burkina.messenger.domain.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PersonalChatService {

    private final ChatRepository chatRepository;
    private final UserChatService userChatService;

    @Transactional
    public void create(PersonalChatCreatedEvent event) {
        Chat chat = Chat.builder()
                .chatId(event.chatId())
                .chatType(ChatType.PERSONAL)
                .build();

        Chat savedChat = chatRepository.save(chat);

        userChatService.createUsersChat(savedChat, List.of(event.user1Id(), event.user2Id()));
    }

    @Transactional
    public void delete(PersonalChatDeletedEvent event) {
        int updated = chatRepository.deleteByChatIdAndChatType(event.chatId(), ChatType.PERSONAL);

        if (updated == 0) {
            log.warn("{} chat {} already deleted or not found", ChatType.PERSONAL, event.chatId());
        }
    }
}
