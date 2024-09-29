package com.kaijoo.demo.util;

import com.kaijoo.demo.service.JwtService;
import com.kaijoo.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

// This class helps us to validate the generated jwt token
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the JWT from cookies
        String token = null;
        Cookie jwtCookie = WebUtils.getCookie(request, "AUTH_TOKEN"); // Assuming "JWT" is the name of your cookie

        if (jwtCookie != null) {
            token = jwtCookie.getValue();
        }

        String username = null;

        if (token != null) {
            username = jwtService.extractEmail(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Check if the token is expiring and renew it
                if (jwtService.isTokenExpiring(token)) {
                    String newToken = jwtService.renewToken(token);

                    if (newToken != null) { // Ensure the new token is not null
                        ResponseCookie cookie = ResponseCookie.from("AUTH_TOKEN", newToken)
                                .httpOnly(true) // To prevent JavaScript access
                                .secure(true) // If using HTTPS
                                .path("/") // Cookie path
                                .maxAge(60 * 60) // Set cookie expiration (1 hour as an example)
                                .build();
                        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}