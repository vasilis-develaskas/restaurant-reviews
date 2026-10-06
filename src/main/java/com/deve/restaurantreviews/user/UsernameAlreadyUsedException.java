package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.shared.exception.ConflictException;

public class UsernameAlreadyUsedException extends ConflictException {
    public UsernameAlreadyUsedException(String username) {
        super("Username: '" + username + "' is already taken");
    }
}
