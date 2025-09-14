package com.bci.auth_service.errors;

public class RequestContextNotFoundException extends RuntimeException {

    public RequestContextNotFoundException() {

        super("No se pudo obtener el request actual");
    }

}

