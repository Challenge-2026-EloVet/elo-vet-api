package com.br.elovetapi.responsible.dtos;

public record UsuarioResponseDTO(
        Long idUsuario,
        String login,
        String email,
        String tipoUsuario
) {
}
