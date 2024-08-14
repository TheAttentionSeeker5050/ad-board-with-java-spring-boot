package com.kaijoo.demo.dto;

public class RegisterResponse {
    public String message;
    public String errorMessage;
    public String authUrl;

    public RegisterResponse(String message, String errorMessage) {
        this.message = message;
        this.errorMessage = errorMessage;
        this.authUrl = "/auth/generateToken";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

}
