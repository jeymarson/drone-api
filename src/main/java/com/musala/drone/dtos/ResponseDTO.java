package com.musala.drone.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private int code;
    private String message;
    private Object data;
    private List<String> errors;

    public ResponseDTO() {
    }

    public ResponseDTO(int code, String message, Object data, List<String> errors) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
