package com.burkina.messenger.outbox.enums;

import lombok.Getter;

@Getter
public enum EventType {
    SEND_MESSAGE_EVENT("send-message"),
    MARK_MESSAGE_AS_READ_EVENT("mark-message-as-read"),
    REMOVE_MESSAGE_EVENT("remove-message");

    private final String topic;

    EventType(String topic) {
        this.topic = topic;
    }
}
