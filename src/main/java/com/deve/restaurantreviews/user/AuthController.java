package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.user.dto.LoginRequest;
import com.deve.restaurantreviews.user.dto.LoginResponse;
import com.deve.restaurantreviews.user.dto.RegisterUserRequest;
import com.deve.restaurantreviews.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    AuthController(UserService userService,
                   AuthenticationManager authenticationManager,
                   TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse created = userService.register((request));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(request.usernameOrEmail(), request.password()));

        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

        TokenService.IssuedToken token = tokenService.issueToken(user);

        return LoginResponse.bearer(token.value(), token.expiresAt());
    }
}
