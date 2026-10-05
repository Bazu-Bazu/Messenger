package com.burkina.messenger.outbox.enums;

import lombok.Getter;

@Getter
public enum EventType {
    MESSAGE_READ_EVENT("message-read-event"),
    MESSAGE_REMOVED_EVENT("message-removed-event"),
    MESSAGES_SENT_EVENT("messages-sent-event");

    private final String topic;

    EventType(String topic) {
        this.topic = topic;
    }
}
