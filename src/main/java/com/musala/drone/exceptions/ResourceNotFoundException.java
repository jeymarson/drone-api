package com.musala.drone.exceptions;

import static com.musala.drone.exceptions.ExceptionMessages.NOT_FOUND_MESSAGE;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entity) {
        super(String.format(NOT_FOUND_MESSAGE, entity));
    }
}
