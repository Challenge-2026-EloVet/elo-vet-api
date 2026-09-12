package com.br.elovetapi.responsible.dtos;

import com.br.elovetapi.pet.dtos.PetResponseDTO;

import java.time.LocalDateTime;

public record ResponsiblePetResponseDTO(
        Long idPetResponsavel,
        Long idResponsavel,
        PetResponseDTO pet,
        LocalDateTime dataElo
) {
}
