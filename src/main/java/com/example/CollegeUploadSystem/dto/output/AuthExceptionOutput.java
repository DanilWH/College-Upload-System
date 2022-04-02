package com.example.CollegeUploadSystem.dto.output;

public class AuthExceptionOutput {
    private String errorMessage;

    public AuthExceptionOutput(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
