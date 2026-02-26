package com.example.demo.User;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
