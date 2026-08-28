package com.br.elovetapi.pet.mapper;

import com.br.elovetapi.pet.dtos.PetRequestDTO;
import com.br.elovetapi.pet.dtos.PetResponseDTO;
import com.br.elovetapi.pet.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {
    public PetResponseDTO toResponse(Pet pet){
        return new PetResponseDTO(
                pet.getEloId(),
                pet.getNome()
        );
    }

    public void updatePetFromRequest(PetRequestDTO petRequestDTO, Pet existingPet) {
        existingPet.setNome(petRequestDTO.nome());
    }

    public Pet toEntity(PetRequestDTO petRequestDTO) {
        Pet pet = new Pet();
        pet.setNome(petRequestDTO.nome());
        return pet;
    }
}
