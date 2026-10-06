package com.deve.restaurantreviews.user.dto;

import com.deve.restaurantreviews.user.Role;
import com.deve.restaurantreviews.user.User;

import java.time.Instant;

public record UserResponse(Long id, String username, Role role, Instant createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.getCreatedAt());
    }

}
