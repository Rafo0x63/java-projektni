package com.prod.hydraulicsystemsmaintenance.exceptions;

public class SerialNumberConflictException extends RuntimeException {
    public SerialNumberConflictException() {
    }

    public SerialNumberConflictException(String message) {
        super(message);
    }

    public SerialNumberConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialNumberConflictException(Throwable cause) {
        super(cause);
    }
}
