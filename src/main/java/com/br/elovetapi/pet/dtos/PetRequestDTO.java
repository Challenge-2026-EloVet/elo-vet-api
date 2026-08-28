package com.br.elovetapi.pet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequestDTO(
        @NotNull(message="Pet name cannot be null")
        @NotBlank(message="Pet name cannot be blank")
        String nome
) {
}
