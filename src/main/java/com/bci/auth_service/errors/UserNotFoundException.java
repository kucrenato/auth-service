package com.bci.auth_service.errors;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {

        super("Usuario no encontrado");
    }

}

