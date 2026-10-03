package com.deve.restaurantreviews.restaurant;

import com.deve.restaurantreviews.TestcontainersConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesAndLoadsRestaurant() {

        Restaurant restaurant = new Restaurant("Trinity", 2, "Navarinou", "Thessaloniki", "Kentro");
        restaurant.setDescription("Burgeradiko");

        Restaurant saved = restaurantRepository.saveAndFlush(restaurant);
        entityManager.clear();
        Restaurant loaded = restaurantRepository.findById(saved.getId()).orElseThrow();

        assertThat(loaded.getId()).isNotNull();
        assertThat(loaded.getName()).isEqualTo("Trinity");
        assertThat(loaded.getDescription()).isEqualTo("Burgeradiko");
        assertThat(loaded.getPhone()).isNull();
        assertThat(loaded.getRatingCount()).isZero();
        assertThat(loaded.getReviewCount()).isZero();
        assertThat(loaded.getAverageRating()).isZero();
        assertThat(loaded.getVersion()).isZero();
        assertThat(loaded.getCreatedAt()).isNotNull();
        assertThat(loaded.getUpdatedAt()).isNotNull();

    }

    @Test
    void rejectsPriceRangeOutsideAllowedValues() {

        Restaurant restaurant = new Restaurant("Trinity", 7, "Navarinou", "Thessaloniki", "Kentro");

        assertThatThrownBy(() -> restaurantRepository.saveAndFlush(restaurant)).isInstanceOf(DataIntegrityViolationException.class);
    }

}
