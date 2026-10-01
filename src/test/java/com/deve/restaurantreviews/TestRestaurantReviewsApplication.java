package com.deve.restaurantreviews;

import org.springframework.boot.SpringApplication;

public class TestRestaurantReviewsApplication {

    public static void main(String[] args) {
        SpringApplication.from(RestaurantReviewsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
