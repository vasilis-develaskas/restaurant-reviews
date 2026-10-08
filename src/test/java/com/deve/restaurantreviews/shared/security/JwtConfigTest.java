package com.deve.restaurantreviews.shared.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

class JwtConfigTest {

    private static final String ISSUER = "restaurant-reviews";

    private final JwtConfig jwtConfig = new JwtConfig();
    private final  JwtProperties properties = propertiesWithSecret(randomSecret());
    private final JwtEncoder encoder = jwtConfig.jwtEncoder(properties);
    private final JwtDecoder decoder = jwtConfig.jwtDecoder(properties);

    @Test
    void decoderAcceptsTokenCreatedByEncoder() {

        //--Given
        Instant now = Instant.now();
        String token = createToken(encoder, ISSUER, now, now.plus(Duration.ofHours(1)));

        //--When
        Jwt jwt = decoder.decode(token);

        //--Then
        assertThat(jwt.getSubject()).isEqualTo("5");
        assertThat(jwt.getClaimAsString("role")).isEqualTo("USER");
        assertThat(jwt.getClaimAsString("iss")).isEqualTo(ISSUER);
    }

    @Test
    void decoderRejectsTokenWithTamperedPayload() {

        //--Given
        Instant now = Instant.now();
        String token = createToken(encoder, ISSUER, now, now.plus(Duration.ofHours(1)));

        //--When
        String[] parts = token.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String forgedPayload = payload.replace("\"USER\"","\"ADMIN\"");
        String forgedToken = parts[0] + "."
                + Base64.getUrlEncoder().withoutPadding()
                        .encodeToString(forgedPayload.getBytes(StandardCharsets.UTF_8))
                + "." + parts[2];

        //--Then
        assertThat(forgedPayload).contains("\"ADMIN\"");
        assertThatThrownBy(() -> decoder.decode(forgedToken)).isInstanceOf(JwtException.class);
    }

    @Test
    void decoderRejectsTokenSignedWirthAnotherKey() {

        //--Given
        JwtEncoder otherEncoder = jwtConfig.jwtEncoder(propertiesWithSecret(randomSecret()));
        Instant now = Instant.now();
        String token = createToken(otherEncoder, ISSUER, now, now.plus(Duration.ofHours(1)));

        //--When/Then
        assertThatThrownBy(() -> decoder.decode(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void decoderRejectsExpiredToken(){

        //--Given
        Instant now = Instant.now();
        String token = createToken(encoder, ISSUER, now.minus(Duration.ofHours(2)), now.minus(Duration.ofHours(1)));

        //--When/Then
        assertThatThrownBy(() -> decoder.decode(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void decoderRejectsTokenFromAnotherIssuer() {

        //--Given
        Instant now = Instant.now();
        String token = createToken(encoder, "another-issuer", now, now.plus(Duration.ofHours(1)));

        //--When/Then
        assertThatThrownBy(() -> decoder.decode(token)).isInstanceOf(JwtException.class);
    }


    private static JwtProperties propertiesWithSecret(String secret) {
        return new JwtProperties(secret, Duration.ofHours(1), ISSUER);
    }

    private static String randomSecret() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String createToken(JwtEncoder jwtEncoder, String issuer, Instant issuedAt, Instant expiresAt) {
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject("5")
                .claim("role", "USER")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
