package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponse {
    public String email;
    public String roles;
    public String errorMessage;

    public UserInfoResponse(String email, String roles) {
        this.email = email;
        this.roles = roles;
        this.errorMessage = null;
    }

    public UserInfoResponse(String email, String roles, String errorMessage) {
        this.email = null;
        this.roles = null;
        this.errorMessage = errorMessage;
    }
}
