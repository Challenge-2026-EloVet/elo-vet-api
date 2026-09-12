package com.br.elovetapi.security.controller;

import com.br.elovetapi.responsible.dtos.ResponsibleRequestDTO;
import com.br.elovetapi.responsible.exceptions.ResponsibleValidationException;
import com.br.elovetapi.responsible.service.ResponsibleService;
import com.br.elovetapi.security.dto.AuthenticationDTO;
import com.br.elovetapi.security.dto.LoginResponseDTO;
import com.br.elovetapi.security.dto.RegisterDTO;
import com.br.elovetapi.security.infra.TokenService;
import com.br.elovetapi.user.model.User;
import com.br.elovetapi.user.model.UserRole;
import com.br.elovetapi.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final ResponsibleService responsibleService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, ResponsibleService responsibleService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.responsibleService = responsibleService;
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.senha());
        var auth = authenticationManager.authenticate(usernamePassword);

        User principal = (User) auth.getPrincipal();
        var token = tokenService.generateToken(principal);

        Long idResponsavel = UserRole.RESPONSAVEL.getRole().equals(principal.getTipoUsuario())
                ? responsibleService.findResponsibleIdByUsuarioId(principal.getIdUsuario()).orElse(null)
                : null;

        return ResponseEntity.ok(new LoginResponseDTO(token, idResponsavel));
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    @Transactional
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO){
        if(this.userRepository.findByEmail(registerDTO.email()).isPresent()) return ResponseEntity.badRequest().build();

        if (registerDTO.tipoUsuario() == UserRole.RESPONSAVEL
                && (registerDTO.nomeCompleto() == null || registerDTO.nomeCompleto().isBlank()
                    || registerDTO.cpf() == null || registerDTO.cpf().isBlank())) {
            throw new ResponsibleValidationException("nomeCompleto e cpf são obrigatórios para o cadastro de um usuário RESPONSAVEL");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.senha());
        User newUser = new User(registerDTO.nomeUsuario(), registerDTO.email(), encryptedPassword, registerDTO.tipoUsuario().getRole());

        this.userRepository.save(newUser);

        if (registerDTO.tipoUsuario() == UserRole.RESPONSAVEL) {
            responsibleService.createResponsible(new ResponsibleRequestDTO(
                    newUser.getIdUsuario(),
                    registerDTO.nomeCompleto(),
                    registerDTO.cpf(),
                    registerDTO.rg(),
                    registerDTO.dataNascimento(),
                    registerDTO.telefone()
            ));
        }

        return ResponseEntity.ok().build();
    }
}
