package com.deve.restaurantreviews.restaurant;

import com.deve.restaurantreviews.restaurant.dto.RestaurantRequest;
import com.deve.restaurantreviews.restaurant.dto.RestaurantResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/restaurants")
class RestaurantController {

    private final RestaurantService restaurantService;

    RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @GetMapping
    Page<RestaurantResponse> list(@ParameterObject Pageable pageable){
        return  restaurantService.list(pageable);
    }

    @GetMapping("/{id}")
    RestaurantResponse getById(@PathVariable Long id){
        return  restaurantService.getById(id);
    }

    @PostMapping
    ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantRequest request){
        RestaurantResponse created = restaurantService.create(request);
        URI location = URI.create("/api/restaurants/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    RestaurantResponse update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request){
        return restaurantService.update(id, request);
    }
}
