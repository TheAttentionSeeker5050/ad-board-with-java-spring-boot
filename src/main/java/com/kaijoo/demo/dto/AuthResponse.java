package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AuthResponse {
    public String authToken;
    public String errorMessage;
    public Date expirationDate;


    public AuthResponse(String authToken, String errorMessage) {
        this.authToken = authToken;
        this.errorMessage = errorMessage;
        this.expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2);

    }

}
