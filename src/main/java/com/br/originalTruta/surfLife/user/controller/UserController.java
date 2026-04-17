package com.br.originalTruta.surfLife.user.controller;

import com.br.originalTruta.surfLife.common.response.ApiResponse;
import com.br.originalTruta.surfLife.user.record.CreateUserRequest;
import com.br.originalTruta.surfLife.user.record.UserResponse;
import com.br.originalTruta.surfLife.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operações de usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success("User created successfully.", userService.create(request));
    }
}
