package com.br.elovetapi.responsible.mapper;

import com.br.elovetapi.pet.dtos.PetResponseDTO;
import com.br.elovetapi.pet.mapper.PetMapper;
import com.br.elovetapi.responsible.dtos.ResponsiblePetResponseDTO;
import com.br.elovetapi.responsible.model.ResponsiblePet;
import org.springframework.stereotype.Component;

@Component
public class ResponsiblePetMapper {

    private final PetMapper petMapper;

    public ResponsiblePetMapper(PetMapper petMapper) {
        this.petMapper = petMapper;
    }

    public ResponsiblePetResponseDTO toResponse(ResponsiblePet responsiblePet) {
        PetResponseDTO petResponseDTO = petMapper.toResponse(responsiblePet.getPet());
        return new ResponsiblePetResponseDTO(
                responsiblePet.getIdPetResponsavel(),
                responsiblePet.getResponsavel().getIdResponsavel(),
                petResponseDTO,
                responsiblePet.getDataElo()
        );
    }
}
