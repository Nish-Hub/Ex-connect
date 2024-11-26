package com.exconnect.loginservice.exception;

public class UserNameNotFoundException extends RuntimeException {
    public UserNameNotFoundException() {
    }

    public UserNameNotFoundException(String message) {
        super(message);
    }

    public UserNameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameNotFoundException(Throwable cause) {
        super(cause);
    }
}
