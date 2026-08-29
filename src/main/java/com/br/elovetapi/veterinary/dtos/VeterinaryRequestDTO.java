package com.br.elovetapi.veterinary.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeterinaryRequestDTO(
        @NotNull(message = "Veterinary name cannot be null")
        @NotBlank(message = "Veterinary name cannot be blank")
        String nome,

        @NotNull(message = "Veterinary CRMV cannot be null")
        Integer crmv
) {
}