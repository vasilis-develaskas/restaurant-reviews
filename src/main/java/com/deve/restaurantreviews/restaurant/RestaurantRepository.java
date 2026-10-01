package com.deve.restaurantreviews.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

}
