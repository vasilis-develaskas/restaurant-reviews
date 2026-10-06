package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.shared.exception.ConflictException;

public class EmailAlreadyUsedException extends ConflictException {

    public EmailAlreadyUsedException() {
        super("Email is already in use");
    }
}
