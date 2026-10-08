package com.deve.restaurantreviews.shared.security;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtPropertiesTest {

    private static final Duration ONE_HOUR = Duration.ofHours(1);
    private static final String ISSUER = "restaurant-reviews";

    @Test
    void acceptsSecretOf32Bytes() {

        //--Given
        String secret = Base64.getEncoder().encodeToString(new byte[32]);

        //--When
        JwtProperties properties = new JwtProperties(secret, ONE_HOUR, ISSUER);

        //--Then
        assertThat(properties.secretBytes()).hasSize(32);
        assertThat(properties.expiration()).isEqualTo(ONE_HOUR);
        assertThat(properties.issuer()).isEqualTo(ISSUER);
    }

    @Test
    void rejectsSecretShorterThan32Bytes() {

        //--Given
        String shortSecret = Base64.getEncoder().encodeToString(new byte[16]);

        //--When/Then
        assertThatThrownBy(() -> new JwtProperties(shortSecret, ONE_HOUR, ISSUER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 32 bytes");
    }

    @Test
    void rejectsSecretThatIsNotBase64() {

        //--Given
        String placeholder = "change-me";

        //-When/Then
        assertThatThrownBy(() -> new JwtProperties(placeholder, ONE_HOUR, ISSUER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("valid Base64");
    }
}
