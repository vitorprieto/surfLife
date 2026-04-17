package com.br.originalTruta.surfLife.auth.controller;

import com.br.originalTruta.surfLife.auth.record.LoginRequest;
import com.br.originalTruta.surfLife.auth.record.LoginResponse;
import com.br.originalTruta.surfLife.auth.record.RegisterRequest;
import com.br.originalTruta.surfLife.auth.service.AuthService;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Registers a new user and returns a JWT token")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.success(
                "User registered successfully.",
                authService.register(request)
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(
                "Login successful.",
                authService.login(request)
        );
    }
}