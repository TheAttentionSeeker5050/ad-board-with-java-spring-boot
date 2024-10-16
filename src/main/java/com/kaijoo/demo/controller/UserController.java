package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.AuthResponse;
import com.kaijoo.demo.dto.RegisterResponse;
import com.kaijoo.demo.dto.AuthRequest;
import com.kaijoo.demo.dto.UserInfoResponse;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
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

    // get token from header using Authorization: Bearer <token>
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserInfoResponse userProfile(@RequestHeader("Authorization") String token) {
        UserInfoResponse response;

        try {
            // Extract email from token
            // take bearer out of token
            String email = jwtService.extractEmail(token);

            // build a json array with the information using the UserInfoDetails class object
            UserInfoDetails userInfoDetails = (UserInfoDetails) service.loadUserByUsername(email);


            // return the json object
            response = new UserInfoResponse(
                    userInfoDetails.getId(),
                    userInfoDetails.getUsername(),
                    userInfoDetails.getAuthorities().toString()
            );

            return response;

        } catch (Exception e) {
            response = new UserInfoResponse(0, null, null, "Invalid token");
            return response;
        }
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/authenticate")
    public @ResponseBody ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        // use dto AuthResponse to return a json object
        AuthResponse response;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String authToken = jwtService.generateToken(authRequest.getEmail());

                response = new AuthResponse(authToken, null);
                return ResponseEntity.ok(response);
            }

            response = new AuthResponse(null, "Invalid credentials");
            return ResponseEntity.badRequest().body(response);

        } catch (UsernameNotFoundException e) {

            response = new AuthResponse(null, "User not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

}