package com.br.elovetapi.responsible.service;

import com.br.elovetapi.responsible.dtos.ResponsibleRequestDTO;
import com.br.elovetapi.responsible.dtos.ResponsibleResponseDTO;
import com.br.elovetapi.responsible.exceptions.ResponsibleNotFoundException;
import com.br.elovetapi.responsible.exceptions.ResponsibleValidationException;
import com.br.elovetapi.responsible.mapper.ResponsibleMapper;
import com.br.elovetapi.responsible.model.Responsible;
import com.br.elovetapi.responsible.repository.ResponsibleRepository;
import com.br.elovetapi.user.model.User;
import com.br.elovetapi.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsibleService {

    private final ResponsibleRepository responsibleRepository;
    private final UserRepository userRepository;
    private final ResponsibleMapper responsibleMapper;

    public ResponsibleService(ResponsibleRepository responsibleRepository, UserRepository userRepository, ResponsibleMapper responsibleMapper) {
        this.responsibleRepository = responsibleRepository;
        this.userRepository = userRepository;
        this.responsibleMapper = responsibleMapper;
    }

    public List<Responsible> getAllResponsibles() {
        return responsibleRepository.findAll();
    }

    public Responsible getResponsibleById(Long id) {
        return responsibleRepository.findById(id)
                .orElseThrow(() -> new ResponsibleNotFoundException("Responsible not found with id: " + id));
    }

    public ResponsibleResponseDTO createResponsible(ResponsibleRequestDTO responsibleRequestDTO) {
        User usuario = userRepository.findById(responsibleRequestDTO.idUsuario())
                .orElseThrow(() -> new ResponsibleValidationException("User not found with id: " + responsibleRequestDTO.idUsuario()));
        Responsible responsible = responsibleMapper.toEntity(responsibleRequestDTO, usuario);
        return responsibleMapper.toResponse(responsibleRepository.save(responsible));
    }

    public ResponsibleResponseDTO updateResponsible(Long id, ResponsibleRequestDTO responsibleRequestDTO) {
        Responsible existingResponsible = getResponsibleById(id);
        responsibleMapper.updateResponsibleFromRequest(responsibleRequestDTO, existingResponsible);
        return responsibleMapper.toResponse(responsibleRepository.save(existingResponsible));
    }

    public void deleteResponsible(Long id) {
        responsibleRepository.delete(getResponsibleById(id));
    }
}
