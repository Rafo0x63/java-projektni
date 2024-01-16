package com.prod.hydraulicsystemsmaintenance.exceptions;

public class UserDoesntExistException extends Exception {
    public UserDoesntExistException() {
    }

    public UserDoesntExistException(String message) {
        super(message);
    }

    public UserDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntExistException(Throwable cause) {
        super(cause);
    }
}
