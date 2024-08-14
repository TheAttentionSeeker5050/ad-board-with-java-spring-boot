package com.kaijoo.demo.dto;

public class AuthResponse {
    public String authToken;
    public String errorMessage;


    public AuthResponse(String authToken, String errorMessage) {
        this.authToken = authToken;
        this.errorMessage = errorMessage;

    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
