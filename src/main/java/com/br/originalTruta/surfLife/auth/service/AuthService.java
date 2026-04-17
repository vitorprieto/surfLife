package com.br.originalTruta.surfLife.auth.service;

import com.br.originalTruta.surfLife.auth.record.LoginRequest;
import com.br.originalTruta.surfLife.auth.record.LoginResponse;
import com.br.originalTruta.surfLife.auth.record.RegisterRequest;
import com.br.originalTruta.surfLife.common.exception.BusinessException;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import com.br.originalTruta.surfLife.security.JwtService;
import com.br.originalTruta.surfLife.user.entity.User;
import com.br.originalTruta.surfLife.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new BusinessException("Email is already in use.");
        }

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.activate();

        User savedUser = userRepository.save(user);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(savedUser);
        String token = jwtService.generateToken(authenticatedUser);

        return new LoginResponse(
                token,
                "Bearer",
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
        );

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new BusinessException("Invalid email or password."));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user);
        String token = jwtService.generateToken(authenticatedUser);

        return new LoginResponse(
                token,
                "Bearer",
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}