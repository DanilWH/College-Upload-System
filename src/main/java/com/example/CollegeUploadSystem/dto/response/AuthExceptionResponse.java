package com.example.CollegeUploadSystem.dto.response;

public class AuthExceptionResponse {
    private String errorMessage;

    public AuthExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
