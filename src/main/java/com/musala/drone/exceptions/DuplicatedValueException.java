package com.musala.drone.exceptions;

import static com.musala.drone.exceptions.ExceptionMessages.DUPLICATED_VALUE_MESSAGE;

public class DuplicatedValueException extends RuntimeException {

    public DuplicatedValueException(String entity, String field) {
        super(String.format(DUPLICATED_VALUE_MESSAGE, entity, field));
    }
}
