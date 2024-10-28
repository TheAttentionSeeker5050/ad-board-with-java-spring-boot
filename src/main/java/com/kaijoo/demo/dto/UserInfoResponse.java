package com.kaijoo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponse {
    public int id;
    public String email;
    public String name;
    public String roles;
    public String errorMessage;

    public UserInfoResponse(int id, String email, String name, String roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles;
        this.errorMessage = null;
    }

    public UserInfoResponse(int id, String email, String name, String roles, String errorMessage) {
        this.id = id;
        this.email = null;
        this.name = null;
        this.roles = null;
        this.errorMessage = errorMessage;
    }
}
