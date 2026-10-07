package com.burkina.messenger.service;

import com.burkina.common.dto.event.messenger.savedChat.SavedChatCreatedEvent;
import com.burkina.common.dto.event.messenger.savedChat.SavedChatDeletedEvent;
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
public class SavedChatService {

    private final ChatRepository chatRepository;
    private final UserChatService userChatService;

    @Transactional
    public void create(SavedChatCreatedEvent event) {
        Chat chat = Chat.builder()
                .chatId(event.chatId())
                .chatType(ChatType.SAVED)
                .name("Saved")
                .build();

        Chat savedChat = chatRepository.save(chat);

        userChatService.createUsersChat(savedChat, List.of(event.userId()));
    }

    @Transactional
    public void delete(SavedChatDeletedEvent event) {
        int updated = chatRepository.deleteByChatIdAndChatType(event.chatId(), ChatType.SAVED);

        if (updated == 0) {
            log.warn("{} chat {} already deleted or not found", ChatType.SAVED, event.chatId());
        }
    }
}
