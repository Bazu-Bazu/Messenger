package com.burkina.messenger.exception;

public class UserIsNotActive extends RuntimeException {

    public UserIsNotActive(String message) {
        super(message);
    }
}
