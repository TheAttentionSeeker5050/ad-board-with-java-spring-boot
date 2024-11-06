package com.kaijoo.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Calendar;

@Component
public class JwtService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        String newToken = token;

        // if token includes bearer <token> then remove bearer, by splitting the token in the space
        if (token.startsWith("Bearer ")) {
            newToken = token.split(" ")[1];
        }

        return extractClaim(newToken, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Check if the token is about to expire, this is used to determine if renewal is needed
    public boolean isTokenExpiring(String token) {
        Date expiration = extractExpiration(token);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expiration);
        calendar.add(Calendar.MINUTE, -5); // Set the threshold (5 minutes before expiration)
        return calendar.getTime().before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String newToken = token;

        // if token includes bearer <token> then remove bearer, by splitting the token in the space
        if (token.startsWith("Bearer ")) {
            newToken = token.split(" ")[1];
        }

        final String email = extractEmail(newToken);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(newToken));
    }

    public String renewToken(String token) {
        if (!isTokenExpired(token)) {
            // Extract email from the old token
            String email = extractEmail(token);
            // Generate a new token
            return generateToken(email);
        }
        return null; // Token can't be renewed if it's expired
    }

    public String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AUTH_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}