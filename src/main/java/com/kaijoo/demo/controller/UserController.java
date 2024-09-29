package com.kaijoo.demo.controller;

import com.kaijoo.demo.dto.AuthResponse;
import com.kaijoo.demo.dto.RegisterResponse;
import com.kaijoo.demo.dto.AuthRequest;
import com.kaijoo.demo.dto.UserInfoResponse;
import com.kaijoo.demo.model.User;
import com.kaijoo.demo.model.UserInfoDetails;
import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // this does not require a token
    @PostMapping("/register")
    public RegisterResponse addNewUser(@RequestBody User userInfo) {
        RegisterResponse response;

        try {
            boolean result = service.addUser(userInfo);

            if (result) {
                response = new RegisterResponse("User added successfully", null);
                return response;
            } else {
                response = new RegisterResponse(null, "User already exists");
                return response;
            }
        } catch (Exception e) {
            response = new RegisterResponse(null, "User already exists");
            return response;
        }
    }

    // get token from header using Authorization: Bearer <token>
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserInfoResponse userProfile(HttpServletRequest request) {
        UserInfoResponse response;

        try {
            // Retrieve the JWT from cookies
            String token = jwtService.getTokenFromCookies(request.getCookies());

            // Extract the email from the token
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

    @PostMapping("/generateToken")
    public AuthResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        // use dto AuthResponse to return a json object
        AuthResponse authResponse;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    ));

            if (authentication.isAuthenticated()) {
                String authToken = jwtService.generateToken(authRequest.getEmail());

                // Create the cookie for the JWT token
                Cookie jwtCookie = new Cookie("AUTH_TOKEN", authToken);
                jwtCookie.setHttpOnly(true); // Prevents JavaScript access
                jwtCookie.setSecure(true); // Use this if your app is running over HTTPS
                jwtCookie.setPath("/"); // Set the cookie path
                jwtCookie.setMaxAge(60 * 60); // Set cookie expiration time (1 hour as an example)

                // Add the cookie to the response
                response.addCookie(jwtCookie);

                // Return a success response without the token
                authResponse = new AuthResponse(null, null, "Login Successful");
                return authResponse;
            }

            authResponse = new AuthResponse(null, "Invalid credentials");
            return authResponse;

        } catch (UsernameNotFoundException e) {
            authResponse = new AuthResponse(null, "User not found");
            return authResponse;
        }
    }

    // renew token
    @PostMapping("/renewToken")
    public AuthResponse renewToken(HttpServletRequest request) {
        AuthResponse response;
        try {
            // Get the token from the cookies
            String token = jwtService.getTokenFromCookies(request.getCookies());

            // Renew the token
            String newToken = jwtService.renewToken(token);

            response = new AuthResponse(newToken, null);
            return response;

        } catch (Exception e) {
            response = new AuthResponse(null, "Invalid token");
            return response;
        }
    }

}