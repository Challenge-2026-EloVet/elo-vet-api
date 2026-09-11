package com.br.elovetapi.responsible.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ResponsibleRequestDTO(
        @NotNull(message = "O ID do usuário não pode ser nulo")
        Long idUsuario,

        @NotBlank(message = "O nome completo não pode ser nulo ou vazio")
        @Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres")
        String nomeCompleto,

        @NotBlank(message = "O CPF não pode ser nulo ou vazio")
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
