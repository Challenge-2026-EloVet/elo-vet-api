package com.br.elovetapi.responsible.dtos;

import java.time.LocalDate;

public record ResponsibleResponseDTO(
        Long idResponsavel,
        Long idUsuario,
        String nomeCompleto,
        String cpf,
        String rg,
        LocalDate dataNascimento,
        String telefone
) {
}
