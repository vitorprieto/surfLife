package com.br.originalTruta.surfLife.user.record;

import com.br.originalTruta.surfLife.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "Full name is required.")
        @Size(max = 150, message = "Full name must have at most 150 characters.")
        String fullName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Email is invalid.")
        @Size(max = 150, message = "Email must have at most 150 characters.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
        String password,

        @NotNull(message = "Role is required.")
        UserRole role
) {
}
