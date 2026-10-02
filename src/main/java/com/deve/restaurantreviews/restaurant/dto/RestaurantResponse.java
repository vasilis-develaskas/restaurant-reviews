package com.deve.restaurantreviews.restaurant.dto;

import com.deve.restaurantreviews.restaurant.Restaurant;

import java.time.Instant;

public record RestaurantResponse(
        Long id,
        String name,
        String description,
        int priceRange,
        String address,
        String city,
        String neighborhood,
        String phone,
        String website,
        Integer establishedYear,
        int ratingCount,
        double averageRating,
        int reviewCount,
        Instant createdAt
) {

    public static  RestaurantResponse from(Restaurant restaurant){
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPriceRange(),
                restaurant.getAddress(),
                restaurant.getCity(),
                restaurant.getNeighborhood(),
                restaurant.getPhone(),
                restaurant.getWebsite(),
                restaurant.getEstablishedYear(),
                restaurant.getRatingCount(),
                Math.round(restaurant.getAverageRating() * 10) / 10.0,
                restaurant.getReviewCount(),
                restaurant.getCreatedAt()
        );
    }
}
