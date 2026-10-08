package com.deve.restaurantreviews.shared.security;

import com.deve.restaurantreviews.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class JwtPropertiesBindingTest {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    void bindsJwtSecretSettingsFromConfiguration() {

        assertThat(jwtProperties.expiration()).isEqualTo(Duration.ofHours(1));
        assertThat(jwtProperties.issuer()).isEqualTo("restaurant-reviews");

        assertThat(jwtProperties.secretBytes()).hasSizeGreaterThanOrEqualTo(32);
    }
}
