package com.deve.restaurantreviews.shared.exception;

public abstract class NotFoundException extends RuntimeException{
    protected NotFoundException(String message){
        super(message);
    }
}
