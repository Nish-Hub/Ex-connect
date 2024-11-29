package com.exconnect.authservice.exceptions;

public class RetriesExhaustedException extends Exception {
    public RetriesExhaustedException() {
        super();
    }

    public RetriesExhaustedException(String message) {
        super(message);
    }

    public RetriesExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetriesExhaustedException(Throwable cause) {
        super(cause);
    }

    protected RetriesExhaustedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
