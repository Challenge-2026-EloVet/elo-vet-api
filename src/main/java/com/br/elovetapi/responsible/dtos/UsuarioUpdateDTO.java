package com.br.elovetapi.responsible.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

// ignoreUnknown: o front pode reenviar o objeto de usuário tal como recebido
// no GET (ex.: campos derivados como "username" ou "tipoUsuario"), que aqui
// são ignorados propositalmente — alteração de papel/role não é feita por
// este endpoint.
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioUpdateDTO(
        @Size(max = 100, message = "O nome de usuário deve ter no máximo 100 caracteres")
        String login,

        @Email(message = "Formato de e-mail inválido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email,

        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password
) {
}
