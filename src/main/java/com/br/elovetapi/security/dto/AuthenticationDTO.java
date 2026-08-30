package com.br.elovetapi.security.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(
        @NotNull(message = "Login is required")
        @NotEmpty(message = "Login cannot be empty")
        String login,
        @NotNull(message = "Password is required")
        @NotEmpty(message = "Password cannot be empty")
        String password
) {
}
