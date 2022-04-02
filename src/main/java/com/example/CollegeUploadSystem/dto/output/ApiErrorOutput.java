package com.example.CollegeUploadSystem.dto.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class ApiErrorOutput {
    private HttpStatus httpStatus;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ApiErrorOutput(HttpStatus httpStatus, String message, List<String> errors) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = errors;
    }

    public ApiErrorOutput(HttpStatus httpStatus, String message, String error) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }

    public ApiErrorOutput(HttpStatus httpStatus, String error) {
        this.httpStatus = httpStatus;
        this.errors = Collections.singletonList(error);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
