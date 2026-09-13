package com.br.elovetapi.responsible.service;

import com.br.elovetapi.responsible.dtos.ResponsibleRequestDTO;
import com.br.elovetapi.responsible.dtos.ResponsibleResponseDTO;
import com.br.elovetapi.responsible.dtos.UsuarioUpdateDTO;
import com.br.elovetapi.responsible.exceptions.ResponsibleNotFoundException;
import com.br.elovetapi.responsible.exceptions.ResponsibleValidationException;
import com.br.elovetapi.responsible.mapper.ResponsibleMapper;
import com.br.elovetapi.responsible.model.Responsible;
import com.br.elovetapi.responsible.repository.ResponsibleRepository;
import com.br.elovetapi.user.model.User;
import com.br.elovetapi.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResponsibleService {

    private final ResponsibleRepository responsibleRepository;
    private final UserRepository userRepository;
    private final ResponsibleMapper responsibleMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponsibleService(ResponsibleRepository responsibleRepository, UserRepository userRepository, ResponsibleMapper responsibleMapper, PasswordEncoder passwordEncoder) {
        this.responsibleRepository = responsibleRepository;
        this.userRepository = userRepository;
        this.responsibleMapper = responsibleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Responsible> getAllResponsibles() {
        return responsibleRepository.findAll();
    }

    public Responsible getResponsibleById(Long id) {
        return responsibleRepository.findById(id)
                .orElseThrow(() -> new ResponsibleNotFoundException("Responsible not found with id: " + id));
    }

    public Optional<Long> findResponsibleIdByUsuarioId(Long idUsuario) {
        return responsibleRepository.findByUsuarioIdUsuario(idUsuario).map(Responsible::getIdResponsavel);
    }

    public ResponsibleResponseDTO createResponsible(ResponsibleRequestDTO responsibleRequestDTO) {
        if (responsibleRequestDTO.idUsuario() == null) {
            throw new ResponsibleValidationException("O ID do usuário não pode ser nulo");
        }
        User usuario = userRepository.findById(responsibleRequestDTO.idUsuario())
                .orElseThrow(() -> new ResponsibleValidationException("User not found with id: " + responsibleRequestDTO.idUsuario()));
        Responsible responsible = responsibleMapper.toEntity(responsibleRequestDTO, usuario);
        return responsibleMapper.toResponse(responsibleRepository.save(responsible));
    }

    public ResponsibleResponseDTO updateResponsible(Long id, ResponsibleRequestDTO responsibleRequestDTO) {
        Responsible existingResponsible = getResponsibleById(id);
        updateUsuarioFromRequest(responsibleRequestDTO, existingResponsible.getUsuario());
        responsibleMapper.updateResponsibleFromRequest(responsibleRequestDTO, existingResponsible);
        return responsibleMapper.toResponse(responsibleRepository.save(existingResponsible));
    }

    private void updateUsuarioFromRequest(ResponsibleRequestDTO request, User usuario) {
        UsuarioUpdateDTO usuarioRequest = request.usuario();
        if (usuario == null || usuarioRequest == null) return;

        if (usuarioRequest.login() != null && !usuarioRequest.login().isBlank()) {
            usuario.setLogin(usuarioRequest.login());
        }

        if (usuarioRequest.email() != null && !usuarioRequest.email().isBlank() && !usuarioRequest.email().equals(usuario.getEmail())) {
            userRepository.findByEmail(usuarioRequest.email())
                    .ifPresent(existingUser -> {
                        throw new ResponsibleValidationException("Já existe um usuário cadastrado com este e-mail: " + usuarioRequest.email());
                    });
            usuario.setEmail(usuarioRequest.email());
        }

        if (usuarioRequest.password() != null && !usuarioRequest.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioRequest.password()));
        }

        userRepository.save(usuario);
    }

    public void deleteResponsible(Long id) {
        responsibleRepository.delete(getResponsibleById(id));
    }
}
