package com.example.CollegeUploadSystem.dto;

public class JwtExceptionResponse {
    private String errorMessage;

    public JwtExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
