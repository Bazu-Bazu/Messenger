package com.burkina.messenger.exception;

public class ChatMemberNotFoundException extends RuntimeException {

    public ChatMemberNotFoundException(String message) {
        super(message);
    }
}
