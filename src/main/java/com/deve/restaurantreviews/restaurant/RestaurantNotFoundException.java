package com.deve.restaurantreviews.restaurant;

import com.deve.restaurantreviews.shared.exception.NotFoundException;

public class RestaurantNotFoundException extends NotFoundException {
    public RestaurantNotFoundException(Long id){
        super("Restaurant with id " + id + "was not found!");
    }
}
