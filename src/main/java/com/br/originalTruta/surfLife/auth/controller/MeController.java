package com.br.originalTruta.surfLife.auth.controller;

import com.br.originalTruta.surfLife.auth.record.CurrentUserResponse;
import com.br.originalTruta.surfLife.auth.service.CurrentUserService;
import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@Tag(name = "Current User", description = "Authenticated user data")
public class MeController {

    private final CurrentUserService currentUserService;

    public MeController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping
    @Operation(summary = "Get current user", description = "Returns the authenticated user data")
    public ApiResponse<CurrentUserResponse> me() {
        AuthenticatedUser user = currentUserService.getAuthenticatedUser();

        CurrentUserResponse response = new CurrentUserResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getRoleEnum().name()
        );

        return ApiResponse.success("Authenticated user loaded successfully.", response);
    }
}