package com.exconnect.loginservice.exception;

public class PasswordNotMatchingException extends RuntimeException {
    public PasswordNotMatchingException() {
    }

    public PasswordNotMatchingException(String message) {
        super(message);
    }

    public PasswordNotMatchingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotMatchingException(Throwable cause) {
        super(cause);
    }
}
