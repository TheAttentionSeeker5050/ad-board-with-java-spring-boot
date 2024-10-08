package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterResponse {
    public String message;
    public String errorMessage;
    public String authUrl;

    public RegisterResponse(String message, String errorMessage) {
        this.message = message;
        this.errorMessage = errorMessage;
        this.authUrl = "/auth/generateToken";
    }

}
