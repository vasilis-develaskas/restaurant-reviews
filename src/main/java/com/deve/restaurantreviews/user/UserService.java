package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.user.dto.RegisterUserRequest;
import com.deve.restaurantreviews.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterUserRequest request) {
        String email = User.normalizeEmail(request.email());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyUsedException();
        }
        if (userRepository.findByUsernameIgnoreCase(request.username()).isPresent()) {
            throw new UsernameAlreadyUsedException(request.username());
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(email, request.username(), passwordHash);
        return UserResponse.from(userRepository.save(user));
    }
}
