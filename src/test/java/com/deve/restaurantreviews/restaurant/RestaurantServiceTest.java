package com.deve.restaurantreviews.restaurant;

import com.deve.restaurantreviews.restaurant.dto.RestaurantRequest;
import com.deve.restaurantreviews.restaurant.dto.RestaurantResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private RestaurantService restaurantService;

    @BeforeEach
    void setup(){
        restaurantService = new RestaurantService(restaurantRepository);
    }

    @Test
    void createMapsAllFieldsAndSavesRestaurant(){

        RestaurantRequest request = new RestaurantRequest("Trinity", "Burgeradiko", 2, "Navarinou", "Thessaloniki", "Kentro", "2105436","https://trinity.com", 2024);
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantResponse response = restaurantService.create(request);

        ArgumentCaptor <Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(captor.capture());
        Restaurant saved = captor.getValue();

        assertThat(saved.getName()).isEqualTo("Trinity");
        assertThat(saved.getDescription()).isEqualTo("Burgeradiko");
        assertThat(saved.getPriceRange()).isEqualTo(2);
        assertThat(saved.getAddress()).isEqualTo("Navarinou");
        assertThat(saved.getCity()).isEqualTo("Thessaloniki");
        assertThat(saved.getNeighborhood()).isEqualTo("Kentro");
        assertThat(saved.getPhone()).isEqualTo("2105436");
        assertThat(saved.getWebsite()).isEqualTo("https://trinity.com");
        assertThat(saved.getEstablishedYear()).isEqualTo(2024);

        assertThat(response.name()).isEqualTo("Trinity");
        assertThat(response.ratingCount()).isZero();
        assertThat(response.reviewCount()).isZero();
    }

    @Test
    void getByIdThrowsWhenRestaurantDoesNotExist() {
        when(restaurantRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.getById(42L)).isInstanceOf(RestaurantNotFoundException.class).hasMessageContaining("42");
    }

    @Test
    void updateChangesFieldsOfExistingRestaurant() {
        Restaurant existing = new Restaurant("existing name", 1, "existing address", "existing city", "existing neighborhood");
        existing.setPhone("210000000");
        when(restaurantRepository.findById(7L)).thenReturn(Optional.of(existing));

        RestaurantRequest request = new RestaurantRequest("new name", null, 3, "new address", "new city","new neighborhood", null, null, null);

        RestaurantResponse response = restaurantService.update(7L, request);

        assertThat(existing.getName()).isEqualTo("new name");
        assertThat(existing.getPriceRange()).isEqualTo(3);
        assertThat(existing.getAddress()).isEqualTo("new address");
        assertThat(existing.getNeighborhood()).isEqualTo("new neighborhood");
        assertThat(existing.getPhone()).isNull();

        assertThat(response.name()).isEqualTo("new name");

        verify(restaurantRepository, never()).save(any());
    }
}
