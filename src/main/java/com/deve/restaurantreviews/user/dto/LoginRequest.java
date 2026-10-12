package com.deve.restaurantreviews.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(max = 255) String usernameOrEmail,
        @NotBlank @Size(max = 255) String password
) {

    @Override
    public String toString() {
        return "LoginRequest[usernameOrEmail=" + usernameOrEmail + ", password=***]";
    }
}
