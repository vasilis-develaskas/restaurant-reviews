package com.deve.restaurantreviews.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI restaurantReviewsOpenApi(){
        return new OpenAPI().info(new Info().title("Restaurant Reviews API").description("API for browsing, rating and reviewing restaurants").version("v1"));
    }
}
