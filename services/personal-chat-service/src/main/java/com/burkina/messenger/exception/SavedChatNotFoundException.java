package com.burkina.messenger.exception;

public class SavedChatNotFoundException extends RuntimeException {

    public SavedChatNotFoundException(String message) {
        super(message);
    }
}
