package com.br.elovetapi.veterinary.mapper;

import com.br.elovetapi.veterinary.dtos.VeterinaryRequestDTO;
import com.br.elovetapi.veterinary.dtos.VeterinaryResponseDTO;
import com.br.elovetapi.veterinary.model.Veterinary;
import org.springframework.stereotype.Component;

@Component
public class VeterinaryMapper {
    public VeterinaryResponseDTO toResponse(Veterinary veterinary) {
        return new VeterinaryResponseDTO(
                veterinary.getId(),
                veterinary.getNome(),
                veterinary.getCrmv()
        );
    }

    public void updateVeterinaryFromRequest(VeterinaryRequestDTO veterinaryRequestDTO, Veterinary existingVeterinary) {
        existingVeterinary.setNome(veterinaryRequestDTO.nome());
        existingVeterinary.setCrmv(veterinaryRequestDTO.crmv());
    }

    public Veterinary toEntity(VeterinaryRequestDTO veterinaryRequestDTO) {
        Veterinary veterinary = new Veterinary();
        veterinary.setNome(veterinaryRequestDTO.nome());
        veterinary.setCrmv(veterinaryRequestDTO.crmv());
        return veterinary;
    }
}