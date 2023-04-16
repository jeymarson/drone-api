package com.musala.drone.exceptions;

public class DuplicatedValueException extends RuntimeException {

    private static final String MESSAGE = "%s. %s should not be duplicated";

    public DuplicatedValueException(String entity, String field) {
        super(String.format(MESSAGE, entity, field));
    }
}
