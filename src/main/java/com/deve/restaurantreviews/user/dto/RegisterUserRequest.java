package com.deve.restaurantreviews.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;

import java.nio.charset.StandardCharsets;

public record RegisterUserRequest(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 3, max = 30) @Pattern(regexp = "^[A-Za-z0-9._-]+$") String username,
        @NotBlank @Size(min = 8, max = 72) String password
) {

    @JsonIgnore
    @AssertTrue(message = "must not eceed 72 bytes")
    public boolean isPasswordWithinBcryptLimit() {
        return password == null || password.getBytes(StandardCharsets.UTF_8).length <= 72;
    }

    @Override
    public String toString() {
        return "RegisterUserRequest[email=" + email + ", username=" + username + ", password=***]";
    }
}
