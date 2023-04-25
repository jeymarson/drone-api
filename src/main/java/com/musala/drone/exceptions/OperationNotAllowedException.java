package com.musala.drone.exceptions;

public class OperationNotAllowedException extends  RuntimeException {

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
