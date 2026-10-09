package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.shared.security.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceTest {

    private static final Instant NOW = Instant.now();
    private static final String ISSUER = "restaurant-reviews";

    private final JwtProperties properties = new JwtProperties(randomSecret(), Duration.ofHours(1), ISSUER);
    private final SecretKey key = new SecretKeySpec(properties.secretBytes(), "HmacSHA256");
    private final JwtEncoder encoder = NimbusJwtEncoder.withSecretKey(key).algorithm(MacAlgorithm.HS256).build();
    private final JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    private final TokenService tokenService = new TokenService(encoder, properties, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void issuesTokenWithExpectedClaims() {

        //--Given
        AuthenticatedUser user = authenticatedUser(5L, Role.USER);

        //--When
        TokenService.IssuedToken issued = tokenService.issueToken(user);
        Jwt jwt = decoder.decode(issued.value());

        //--Then
        Instant expectedIssuedAt = NOW.truncatedTo(ChronoUnit.SECONDS);
        Instant expectedExpiresAt = expectedIssuedAt.plus(Duration.ofHours(1));

        assertThat(jwt.getSubject()).isEqualTo("5");
        assertThat(jwt.getClaimAsString("role")).isEqualTo("USER");
        assertThat(jwt.getClaimAsString("iss")).isEqualTo(ISSUER);
        assertThat(jwt.getIssuedAt()).isEqualTo(expectedIssuedAt);
        assertThat(jwt.getExpiresAt()).isEqualTo(expectedExpiresAt);

        assertThat((issued.expiresAt())).isEqualTo(jwt.getExpiresAt());
    }

    @Test
    void adminTokenHasAdminRole() {

        //--Given
        AuthenticatedUser admin = authenticatedUser(1L, Role.ADMIN);

        //--When
        Jwt jwt = decoder.decode(tokenService.issueToken(admin).value());

        //--Then
        assertThat(jwt.getClaimAsString("role")).isEqualTo("ADMIN");
    }

    @Test
    void tokenContainsNoPersonalData() {

        //--Given
        AuthenticatedUser user = authenticatedUser(5L, Role.USER);

        //--When
        Jwt jwt = decoder.decode(tokenService.issueToken(user).value());

        //--Then
        assertThat(jwt.getClaims()).containsOnlyKeys("iss", "sub", "role", "iat", "exp");
    }

    @Test
    void refusesToIssueTokenForUserWithoutId() {

        //--Given
        AuthenticatedUser unsaved = AuthenticatedUser.from(new User("maria@example.com", "Maria", "hash"));

        //--When/Then
        assertThatThrownBy(() -> tokenService.issueToken(unsaved))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void issuedTokenToStringDoesNotExposeTokenValue() {

        TokenService.IssuedToken issued = tokenService.issueToken(authenticatedUser(5L, Role.USER));

        assertThat(issued.toString()).doesNotContain(issued.value());
    }

    private static AuthenticatedUser authenticatedUser(Long id, Role role) {
        User user = new User("maria@example.com", "Maria", "hashed-password");
        user.changeRole(role);

        ReflectionTestUtils.setField(user, "id", id);
        return AuthenticatedUser.from(user);
    }

    private static String randomSecret() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
