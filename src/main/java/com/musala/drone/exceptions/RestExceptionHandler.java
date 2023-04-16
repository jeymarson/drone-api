package com.musala.drone.exceptions;

import com.musala.drone.dtos.ResponseDTO;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {PropertyValueException.class})
    protected ResponseEntity<ResponseDTO> handleNotNullProperty(PropertyValueException exception) {

        String error = String.format(ExceptionMessages.NOT_NULL_FIELD_MESSAGE, exception.getPropertyName());
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.BAD_REQUEST.value(), exception.getLocalizedMessage(), null, Collections.singletonList(error));

        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<ResponseDTO> handleNotNullProperty(ResourceNotFoundException exception) {

        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.NOT_FOUND.value(), exception.getLocalizedMessage(), null, Collections.singletonList(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
