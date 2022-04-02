package com.example.CollegeUploadSystem.handlers;

import com.example.CollegeUploadSystem.dto.output.ApiErrorOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiErrorOutput apiErrorOutput = new ApiErrorOutput(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), errors);

        return handleExceptionInternal(ex, apiErrorOutput, headers, apiErrorOutput.getHttpStatus(), request);
    }
/*

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorOutput apiErrorOutput = new ApiErrorOutput(HttpStatus.NOT_FOUND, "The resource with such an ID was not found");

        return handleExceptionInternal(ex, apiErrorOutput, headers, apiErrorOutput.getHttpStatus(), request);
    }
*/
}
