package com.burkina.messenger.outbox.enums;

import lombok.Getter;

@Getter
public enum EventType {
    GROUP_CHAT_CREATED("group-chat-created"),
    GROUP_CHAT_DELETED("group-chat-deleted"),
    GROUP_CHAT_INFO_UPDATED("group-chat-info-updated"),

    GROUP_CHAT_MEMBERS_UPDATED("group-chat-members-updated"),
    GROUP_CHAT_MEMBERS_DELETED("group-chat-members-deleted"),
    GROUP_CHAT_MEMBERS_ADDED("group-chat-members-added");

    private final String topic;

    EventType(String topic) {
        this.topic = topic;
    }
}
