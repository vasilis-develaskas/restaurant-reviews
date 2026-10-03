package com.deve.restaurantreviews.restaurant;

import com.deve.restaurantreviews.restaurant.dto.RestaurantRequest;
import com.deve.restaurantreviews.restaurant.dto.RestaurantResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    RestaurantService(RestaurantRepository restaurantRepository){
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public RestaurantResponse create(RestaurantRequest request){
        Restaurant restaurant = new Restaurant(
                request.name(),
                request.priceRange(),
                request.address(),
                request.city(),
                request.neighborhood()
        );
        applyOptionalFields(restaurant, request);

        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getById(Long id) {
        return RestaurantResponse.from(findRestaurant(id));
    }

    @Transactional(readOnly = true)
    public Page<RestaurantResponse> list(Pageable pageable){
        return restaurantRepository.findAll(pageable).map(RestaurantResponse::from);
    }

    @Transactional
    public RestaurantResponse update(Long id, RestaurantRequest request){
        Restaurant restaurant = findRestaurant(id);
        restaurant.setName(request.name());
        restaurant.setPriceRange(request.priceRange());
        restaurant.setAddress(request.address());
        restaurant.setCity(request.city());
        restaurant.setNeighborhood(request.neighborhood());
        applyOptionalFields(restaurant, request);

        return  RestaurantResponse.from(restaurant);
    }

    private  Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    private void applyOptionalFields(Restaurant restaurant, RestaurantRequest request){
        restaurant.setDescription(request.description());
        restaurant.setPhone(request.phone());
        restaurant.setWebsite(request.website());
        restaurant.setEstablishedYear(request.establishedYear());
    }
}
