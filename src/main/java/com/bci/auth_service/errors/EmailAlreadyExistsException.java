package com.bci.auth_service.errors;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {

        super(message);
    }

}