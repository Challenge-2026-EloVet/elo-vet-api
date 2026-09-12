package com.br.elovetapi.responsible.service;

import com.br.elovetapi.pet.exceptions.PetNotFoundException;
import com.br.elovetapi.pet.mapper.PetMapper;
import com.br.elovetapi.pet.model.Pet;
import com.br.elovetapi.pet.repository.PetRepository;
import com.br.elovetapi.responsible.dtos.ResponsiblePetRequestDTO;
import com.br.elovetapi.responsible.dtos.ResponsiblePetResponseDTO;
import com.br.elovetapi.responsible.exceptions.ResponsibleNotFoundException;
import com.br.elovetapi.responsible.exceptions.ResponsiblePetNotFoundException;
import com.br.elovetapi.responsible.exceptions.ResponsibleValidationException;
import com.br.elovetapi.responsible.mapper.ResponsiblePetMapper;
import com.br.elovetapi.responsible.model.Responsible;
import com.br.elovetapi.responsible.model.ResponsiblePet;
import com.br.elovetapi.responsible.repository.ResponsiblePetRepository;
import com.br.elovetapi.responsible.repository.ResponsibleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponsiblePetService {

    private final ResponsiblePetRepository responsiblePetRepository;
    private final ResponsibleRepository responsibleRepository;
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final ResponsiblePetMapper responsiblePetMapper;

    public ResponsiblePetService(ResponsiblePetRepository responsiblePetRepository,
                                  ResponsibleRepository responsibleRepository,
                                  PetRepository petRepository,
                                  PetMapper petMapper,
                                  ResponsiblePetMapper responsiblePetMapper) {
        this.responsiblePetRepository = responsiblePetRepository;
        this.responsibleRepository = responsibleRepository;
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.responsiblePetMapper = responsiblePetMapper;
    }

    public List<ResponsiblePetResponseDTO> getPetsByResponsible(Long idResponsavel) {
        return responsiblePetRepository.findByResponsavelIdResponsavel(idResponsavel).stream()
                .map(responsiblePetMapper::toResponse)
                .toList();
    }

    @Transactional
    public ResponsiblePetResponseDTO linkPetToResponsible(ResponsiblePetRequestDTO requestDTO) {
        if (requestDTO.idPet() == null && requestDTO.pet() == null) {
            throw new ResponsibleValidationException("Informe o idPet de um pet existente ou os dados do pet para criação");
        }
        if (requestDTO.idPet() != null && requestDTO.pet() != null) {
            throw new ResponsibleValidationException("Informe apenas o idPet ou os dados do pet, não ambos");
        }

        Responsible responsavel = responsibleRepository.findById(requestDTO.idResponsavel())
                .orElseThrow(() -> new ResponsibleNotFoundException("Responsible not found with id: " + requestDTO.idResponsavel()));

        Pet pet = requestDTO.idPet() != null
                ? petRepository.findById(requestDTO.idPet())
                        .orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + requestDTO.idPet()))
                : petRepository.save(petMapper.toEntity(requestDTO.pet()));

        if (responsiblePetRepository.existsByPetIdPetAndResponsavelIdResponsavel(pet.getIdPet(), responsavel.getIdResponsavel())) {
            throw new ResponsibleValidationException("Pet already linked to this responsible");
        }

        ResponsiblePet responsiblePet = new ResponsiblePet();
        responsiblePet.setPet(pet);
        responsiblePet.setResponsavel(responsavel);
        responsiblePet.setDataElo(LocalDateTime.now());

        return responsiblePetMapper.toResponse(responsiblePetRepository.save(responsiblePet));
    }

    public void unlinkPet(Long idPetResponsavel) {
        ResponsiblePet responsiblePet = responsiblePetRepository.findById(idPetResponsavel)
                .orElseThrow(() -> new ResponsiblePetNotFoundException("Pet-Responsible link not found with id: " + idPetResponsavel));
        responsiblePetRepository.delete(responsiblePet);
    }
}
