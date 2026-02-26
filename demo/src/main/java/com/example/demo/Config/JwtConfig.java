package com.example.demo.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class JwtConfig {
    private final String secret;
    private final long expirationMinutes;

    public JwtConfig(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMinutes
    ) {
        this.secret = secret;
        this.expirationMinutes = expirationMinutes;
    }
}
