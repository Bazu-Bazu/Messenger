package com.burkina.messenger.mapper;

import com.burkina.common.dto.event.messenger.savedChat.SavedChatCreatedEvent;
import com.burkina.common.dto.event.messenger.savedChat.SavedChatDeletedEvent;
import com.burkina.messenger.domain.entity.SavedChat;
import com.burkina.messenger.dto.response.SavedChatResponse;
import org.springframework.stereotype.Component;

@Component
public class SavedChatMapper {

    public SavedChatResponse toResponse(SavedChat chat) {
        return SavedChatResponse.builder()
                .id(chat.getId())
                .userId(chat.getUserId())
                .createdAt(chat.getCreatedAt())
                .build();
    }

    public SavedChatCreatedEvent toSavedChatCreatedEvent(SavedChat chat) {
        return SavedChatCreatedEvent.builder()
                .chatId(chat.getId())
                .userId(chat.getUserId())
                .build();
    }

    public SavedChatDeletedEvent toSavedChatDeletedEvent(SavedChat chat) {
        return SavedChatDeletedEvent.builder()
                .chatId(chat.getId())
                .build();
    }
}
