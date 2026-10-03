package com.burkina.messenger.mapper;

import com.burkina.common.dto.event.messenger.personalChat.PersonalChatCreatedEvent;
import com.burkina.common.dto.event.messenger.personalChat.PersonalChatDeletedEvent;
import com.burkina.messenger.domain.entity.PersonalChat;
import com.burkina.messenger.dto.response.PersonalChatResponse;
import org.springframework.stereotype.Component;

@Component
public class PersonalChatMapper {

    public PersonalChatResponse toResponse(PersonalChat chat) {
        return PersonalChatResponse.builder()
                .id(chat.getId())
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .createdAt(chat.getCreatedAt())
                .build();
    }

    public PersonalChatCreatedEvent toPersonalChatCreatedEvent(PersonalChat chat) {
        return PersonalChatCreatedEvent.builder()
                .chatId(chat.getId())
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .build();
    }

    public PersonalChatDeletedEvent toPersonalChatDeletedEvent(PersonalChat chat) {
        return PersonalChatDeletedEvent.builder()
                .chatId(chat.getId())
                .build();
    }
}
