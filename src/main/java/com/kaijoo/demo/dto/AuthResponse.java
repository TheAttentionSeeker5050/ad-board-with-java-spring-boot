package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    public String authToken;
    public String errorMessage;
    public String successMessage;


    public AuthResponse(String authToken, String errorMessage, String successMessage) {
        this.authToken = authToken;
        this.errorMessage = errorMessage;
        this.successMessage = successMessage;

    }

    public AuthResponse(String authToken, String errorMessage) {
        this.authToken = authToken;
        this.errorMessage = errorMessage;

    }

}
