package com.example.demo.Config;

import com.example.demo.User.UserTable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtHelper {
    private final Key SECRET_KEY;
    private final long MINUTES;

    public JwtHelper(JwtConfig config) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(config.getSecret().getBytes());
        this.MINUTES = config.getExpirationMinutes();
    }

    public String generateToken(String email) {
        var now = Instant.now();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractEmail(String token) {
        return getTokenBody(token).getSubject();
    }

    public Boolean validateToken(String token, UserTable userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getEmail()) && !isTokenExpired(token);
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts.parserBuilder().build().parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }
}
