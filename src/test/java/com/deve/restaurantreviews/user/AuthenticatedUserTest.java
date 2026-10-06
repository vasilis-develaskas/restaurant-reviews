package com.deve.restaurantreviews.user;


import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticatedUserTest {

    @Test
    void mapsUserFieldsAndRoleToAuthority() {

        //--Given
        User user = new User("maria@example.com", "Maria", "hashed-password");

        //--When
        AuthenticatedUser authenticatedUser = AuthenticatedUser.from(user);

        //--Then
        assertThat(authenticatedUser.getUsername()).isEqualTo("Maria");
        assertThat(authenticatedUser.getPassword()).isEqualTo("hashed-password");
        assertThat(authenticatedUser.getRole()).isEqualTo(Role.USER);
        assertThat(authenticatedUser.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_USER");
    }

    @Test
    void adminGetsAdminAuthority() {

        //--Given
        User user = new User("admin@example.com", "admin", "hashed-password");
        user.changeRole(Role.ADMIN);

        //--When
        AuthenticatedUser authenticatedUser = AuthenticatedUser.from(user);

        //--Then
        assertThat(authenticatedUser.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_ADMIN");
    }

    @Test
    void toStringDoesNotExposePasswordHash() {

        //--Given
        User user = new User("maria@example.com", "Maria", "hashed-password");

        //--When
        AuthenticatedUser authenticatedUser = AuthenticatedUser.from(user);

        //--Then
        assertThat(authenticatedUser.toString())
                .contains("Maria")
                .doesNotContain("hashed-password");
    }
}
