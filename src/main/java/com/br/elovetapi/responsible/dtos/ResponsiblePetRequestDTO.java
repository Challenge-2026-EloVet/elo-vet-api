package com.br.elovetapi.responsible.dtos;

import com.br.elovetapi.pet.dtos.PetRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ResponsiblePetRequestDTO(
        @NotNull(message = "O ID do responsável não pode ser nulo")
        Long idResponsavel,

        Long idPet,

        @Valid
        PetRequestDTO pet
) {
}
