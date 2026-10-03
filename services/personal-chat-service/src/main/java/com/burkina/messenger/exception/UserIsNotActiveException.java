package com.burkina.messenger.exception;

public class UserIsNotActiveException extends RuntimeException {

    public UserIsNotActiveException(String message) {
        super(message);
    }
}
