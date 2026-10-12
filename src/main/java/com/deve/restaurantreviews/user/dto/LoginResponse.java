package com.deve.restaurantreviews.user.dto;

import java.time.Instant;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt
) {

    private static final String BEARER = "Bearer";

    public static LoginResponse bearer(String accessToken,  Instant expiresAt) {
        return new LoginResponse(accessToken, BEARER, expiresAt);
    }

    @Override
    public String toString() {
        return "LoginResponse[accessToken=***, tokenType=" + tokenType + ", expiresAt=" + expiresAt + "]";
    }
}
