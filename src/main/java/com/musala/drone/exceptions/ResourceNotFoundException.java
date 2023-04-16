package com.musala.drone.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE = "%s not found";

    public ResourceNotFoundException(String entity) {
        super(String.format(MESSAGE, entity));
    }
}
