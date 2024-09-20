package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponse {
    public int id;
    public String email;
    public String roles;
    public String errorMessage;

    public UserInfoResponse(int id, String email, String roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.errorMessage = null;
    }

    public UserInfoResponse(int id, String email, String roles, String errorMessage) {
        this.id = id;
        this.email = null;
        this.roles = null;
        this.errorMessage = errorMessage;
    }
}
