package com.kaijoo.demo.dto;

import lombok.*;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    // Getters and Setters
    private String email;
    private String password;

}