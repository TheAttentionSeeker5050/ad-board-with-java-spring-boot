package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.AuthResponse;
import com.kaijoo.demo.dto.RegisterResponse;
import com.kaijoo.demo.dto.AuthRequest;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public RegisterResponse addNewUser(@RequestBody User userInfo) {
        boolean result = service.addUser(userInfo);

        RegisterResponse response;

        if (result) {
            response = new RegisterResponse("User added successfully", null);
            return response;
        } else {
            response = new RegisterResponse(null, "User already exists");
            return response;
        }
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        // use dto AuthResponse to return a json object
        AuthResponse response;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String authToken = jwtService.generateToken(authRequest.getEmail());

                response = new AuthResponse(authToken, null);
                return response;
            }

            response = new AuthResponse(null, "Invalid credentials");
            return response;

        } catch (UsernameNotFoundException e) {

            response = new AuthResponse(null, "User not found");
            return response;
        }
    }

}