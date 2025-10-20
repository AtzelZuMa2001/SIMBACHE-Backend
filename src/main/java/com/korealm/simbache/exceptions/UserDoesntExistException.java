package com.korealm.simbache.exceptions;

public class UserDoesntExistException extends RuntimeException {
    public UserDoesntExistException(String message) {
        super(message);
    }
}
