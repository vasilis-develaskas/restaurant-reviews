package com.deve.restaurantreviews.shared.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Base64;

@Validated
@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(
        @NotBlank String secret,
        @NotNull Duration expiration,
        @NotBlank String issuer
        ) {

    private static final int MIN_SECRET_BYTES = 32;

    public JwtProperties {
        if (secret != null && !secret.isBlank()) {
            byte[] decoded = decodeSecret(secret);
            if (decoded.length < MIN_SECRET_BYTES) {
                throw new IllegalArgumentException("JWT secret must be at least " + MIN_SECRET_BYTES + " bytes after Base64 decoding!");
            }
        }
    }

    public  byte[] secretBytes() {
        return decodeSecret(secret);
    }

    private static byte[] decodeSecret(String secret) {
        try {
            return Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT secret must be a valid Base64 string", e);
        }
    }
}
