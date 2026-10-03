package com.burkina.messenger.outbox.enums;

import lombok.Getter;

@Getter
public enum EventType {
    PERSONAL_CHAT_CREATED("personal-chat-created"),
    PERSONAL_CHAT_DELETED("personal-chat-deleted"),

    SAVED_CHAT_CREATED("saved-chat-created"),
    SAVED_CHAT_DELETED("saved-chat-deleted");

    private final String topic;

    EventType(String topic) {
        this.topic = topic;
    }
}
