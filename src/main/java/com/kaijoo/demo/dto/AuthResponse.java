package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
    public String authToken;
    public String errorMessage;


    public AuthResponse(String authToken, String errorMessage) {
        this.authToken = authToken;
        this.errorMessage = errorMessage;

    }

}
