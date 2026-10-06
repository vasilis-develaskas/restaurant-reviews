package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.user.dto.RegisterUserRequest;
import com.deve.restaurantreviews.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerStoresHashedPasswordAndNormalizedEmail() {

        //--Given
        RegisterUserRequest request = new RegisterUserRequest("Maria@Example.com", "Maria", "secret123");
        when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsernameIgnoreCase("Maria")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //--When
        UserResponse response = userService.register(request);

        //--Then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertThat(saved.getPasswordHash()).isEqualTo("hashed-password");
        assertThat(saved.getPasswordHash()).isNotEqualTo("secret123");
        assertThat(saved.getEmail()).isEqualTo("maria@example.com");
        assertThat(saved.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void registerRejectsEmailAlreadyInUse() {

        //--Given
        RegisterUserRequest request = new RegisterUserRequest("maria@example.com", "Maria", "secret123");
        when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(new User("maria@example.com", "Other", "hash")));

        //--When/Then
        assertThatThrownBy(() -> userService.register(request)).isInstanceOf(EmailAlreadyUsedException.class);
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());

    }

    @Test
    void registerRejectsUsernameAlreadyInuse() {

        //--Given
        RegisterUserRequest request = new RegisterUserRequest("maria@example.com", "maria", "secret123");
        when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsernameIgnoreCase("maria")).thenReturn(Optional.of(new User("other@example.com", "Maria","hash")));

        //--When/Then
        assertThatThrownBy(() -> userService.register(request)).isInstanceOf(UsernameAlreadyUsedException.class).hasMessageContaining("maria");
        verify(userRepository, never()).save(any());
    }
}
