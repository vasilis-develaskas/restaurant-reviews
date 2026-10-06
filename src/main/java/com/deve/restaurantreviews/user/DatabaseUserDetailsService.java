package com.deve.restaurantreviews.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String loginIdentifier) throws UsernameNotFoundException {
        String identifier = loginIdentifier.trim();

        Optional<User> foundUser = identifier.contains("@")
                ? userRepository.findByEmail(User.normalizeEmail(identifier))
                : userRepository.findByUsernameIgnoreCase(identifier);

        return foundUser
                .map(AuthenticatedUser::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
