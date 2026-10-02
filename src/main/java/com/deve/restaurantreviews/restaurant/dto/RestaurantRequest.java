package com.deve.restaurantreviews.restaurant.dto;

import jakarta.validation.constraints.*;

public record RestaurantRequest(
    @NotBlank @Size(max = 100) String name,
    @Size(max = 2000) String description,
    @NotNull @Min(1) @Max(5) Integer priceRange,
    @NotBlank @Size(max = 200) String address,
    @NotBlank @Size(max = 100) String city,
    @NotBlank @Size(max = 100) String neighborhood,
    @Size(max = 20) String phone,
    @Size(max = 200) String website,
    @Min(1800) Integer establishedYear
    ){
}
