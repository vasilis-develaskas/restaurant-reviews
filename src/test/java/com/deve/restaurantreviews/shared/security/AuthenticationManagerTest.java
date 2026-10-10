package com.deve.restaurantreviews.shared.security;

import com.deve.restaurantreviews.TestcontainersConfiguration;
import com.deve.restaurantreviews.user.AuthenticatedUser;
import com.deve.restaurantreviews.user.UserService;
import com.deve.restaurantreviews.user.dto.RegisterUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class AuthenticationManagerTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @BeforeEach
    void registerUser() {
        userService.register(new RegisterUserRequest("maria@example.com", "Maria", "secret123"));
    }

    @Test
    void authenticatesWithUsernameAndCorrectPassword() {

        //--Given
        Authentication request = UsernamePasswordAuthenticationToken.unauthenticated("maria", "secret123");

        //--When
        Authentication result = authenticationManager.authenticate(request);

        //--Then
        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getPrincipal()).isInstanceOf(AuthenticatedUser.class);

        AuthenticatedUser user = (AuthenticatedUser) result.getPrincipal();
        assertThat(user.getUsername()).isEqualTo("Maria");
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_USER", "FACTOR_PASSWORD");

        assertThat(result.getCredentials()).isNull();
    }

    @Test
    void authenticatesWithEmail() {

        //--Given
        Authentication request = UsernamePasswordAuthenticationToken.unauthenticated("MARIA@example.com", "secret123");

        //--When
        Authentication result = authenticationManager.authenticate(request);

        //--Then
        assertThat(result.isAuthenticated()).isTrue();
    }

    @Test
    void rejectsWrongPassword() {
        Authentication request = UsernamePasswordAuthenticationToken.unauthenticated("maria", "wrong-password");

        assertThatThrownBy(() -> authenticationManager.authenticate(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void unknownUserFailsExactlyLikeWrongPassword() {

        //--When
        Throwable wrongPassword = catchThrowable(() -> authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated("maria", "wrong-password")));
        Throwable unknownUser = catchThrowable(() -> authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated("nikos", "secret123")));

        //--Then
        assertThat(wrongPassword).isExactlyInstanceOf(BadCredentialsException.class);
        assertThat(unknownUser).isExactlyInstanceOf(BadCredentialsException.class);
        assertThat(unknownUser.getMessage()).isEqualTo(wrongPassword.getMessage());
    }

}
