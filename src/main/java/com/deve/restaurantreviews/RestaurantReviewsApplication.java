package com.deve.restaurantreviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RestaurantReviewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantReviewsApplication.class, args);
    }

}
