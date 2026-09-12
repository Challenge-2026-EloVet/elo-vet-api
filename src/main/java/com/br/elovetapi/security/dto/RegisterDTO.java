package com.br.elovetapi.security.dto;

import com.br.elovetapi.user.model.UserRole;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterDTO(
        @NotBlank(message = "O nome de usuário não pode ser nulo ou vazio")
        @Size(max = 100, message = "O nome de usuário deve ter no máximo 100 caracteres")
        String nomeUsuario,

        @NotBlank(message = "O e-mail não pode ser nulo ou vazio")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email,

        @NotBlank(message = "A senha não pode ser nula ou vazia")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotNull(message = "O tipo de usuário não pode ser nulo")
        UserRole tipoUsuario,

        // Campos abaixo são obrigatórios apenas quando tipoUsuario = RESPONSAVEL
        @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres")
        String nomeCompleto,

        @Pattern(regexp = "^[0-9]{11}$", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        String cpf,

        @Size(max = 20, message = "O RG deve ter no máximo 20 caracteres")
        String rg,

        @Past(message = "A data de nascimento deve ser uma data passada")
        LocalDate dataNascimento,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone
) {
}
