package com.br.elovetapi.security.dto;

import com.br.elovetapi.user.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
        @NotNull(message = "Login cannot be null")
        @NotBlank(message = "Login cannot be blank")
        String login,
        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be blank")
        String password,
        UserRole role
) {
}
