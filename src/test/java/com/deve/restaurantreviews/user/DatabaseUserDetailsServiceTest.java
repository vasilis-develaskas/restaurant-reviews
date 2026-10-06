package com.deve.restaurantreviews.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private DatabaseUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new DatabaseUserDetailsService(userRepository);
    }

    @Test
    void loadUserByEmailUsingNormalizedEmail() {

        //--Given
        User user = new User("maria@example.com", "Maria", "hashed-password");
        when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(user));

        //--When
        UserDetails result = userDetailsService.loadUserByUsername(" Maria@Example.COM");

        //--Then
        assertThat(result.getUsername()).isEqualTo("Maria");
        assertThat(result.getPassword()).isEqualTo("hashed-password");
        assertThat(result.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .contains("ROLE_USER");
        verify(userRepository, never()).findByUsernameIgnoreCase(any());
    }

    @Test
    void loadsUserByUsername() {

        //--Given
        User user = new User("maria@example.com", "Maria", "hashed-password");
        when(userRepository.findByUsernameIgnoreCase("maria")).thenReturn(Optional.of(user));

        //--When
        UserDetails result = userDetailsService.loadUserByUsername(" maria ");

        //--Then
        assertThat(result.getUsername()).isEqualTo("Maria");
        verify(userRepository,never()).findByEmail(any());
    }

    @Test
    void throwsWhenUserDoesNotExist() {

        //--Given
        when(userRepository.findByUsernameIgnoreCase("nikos")).thenReturn(Optional.empty());

        //-- When/Then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nikos"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
