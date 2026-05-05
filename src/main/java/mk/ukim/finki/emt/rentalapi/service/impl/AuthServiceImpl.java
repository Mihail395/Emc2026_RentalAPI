package mk.ukim.finki.emt.rentalapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.emt.rentalapi.config.JwtUtil;
import mk.ukim.finki.emt.rentalapi.model.domain.User;
import mk.ukim.finki.emt.rentalapi.model.dto.AuthResponse;
import mk.ukim.finki.emt.rentalapi.model.dto.LoginRequest;
import mk.ukim.finki.emt.rentalapi.model.dto.RegisterRequest;
import mk.ukim.finki.emt.rentalapi.model.enums.Role;
import mk.ukim.finki.emt.rentalapi.repository.UserRepository;
import mk.ukim.finki.emt.rentalapi.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check if username already exists
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists: " + request.username());
        }

        // Determine role — default to ROLE_USER if not specified
        Role role = Role.ROLE_USER;
        if (request.role() != null && request.role().equalsIgnoreCase("ROLE_ADMIN")) {
            role = Role.ROLE_ADMIN;
        }

        // Create and save the user
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(role);
        userRepository.save(user);

        // Generate and return the token
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // AuthenticationManager verifies username + password
        // Throws an exception automatically if credentials are wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // If we reach here, authentication was successful
        // Load the user and generate a token
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}