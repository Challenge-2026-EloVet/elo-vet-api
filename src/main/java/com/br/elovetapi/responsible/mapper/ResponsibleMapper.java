package com.br.elovetapi.responsible.mapper;

import com.br.elovetapi.responsible.dtos.ResponsibleRequestDTO;
import com.br.elovetapi.responsible.dtos.ResponsibleResponseDTO;
import com.br.elovetapi.responsible.dtos.UsuarioResponseDTO;
import com.br.elovetapi.responsible.model.Responsible;
import com.br.elovetapi.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class ResponsibleMapper {
    public ResponsibleResponseDTO toResponse(Responsible responsible) {
        return new ResponsibleResponseDTO(
                responsible.getIdResponsavel(),
                responsible.getUsuario() != null ? responsible.getUsuario().getIdUsuario() : null,
                responsible.getNomeCompleto(),
                responsible.getCpf(),
                responsible.getRg(),
                responsible.getDataNascimento(),
                responsible.getTelefone(),
                toUsuarioResponse(responsible.getUsuario())
        );
    }

    private UsuarioResponseDTO toUsuarioResponse(User usuario) {
        if (usuario == null) return null;
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getLogin(),
                usuario.getEmail(),
                usuario.getTipoUsuario()
        );
    }

    public void updateResponsibleFromRequest(ResponsibleRequestDTO request, Responsible existingResponsible) {
        existingResponsible.setNomeCompleto(request.nomeCompleto());
        existingResponsible.setCpf(request.cpf());
        existingResponsible.setRg(request.rg());
        existingResponsible.setDataNascimento(request.dataNascimento());
        existingResponsible.setTelefone(request.telefone());
    }

    public Responsible toEntity(ResponsibleRequestDTO request, User usuario) {
        Responsible responsible = new Responsible();
        responsible.setUsuario(usuario);
        responsible.setNomeCompleto(request.nomeCompleto());
        responsible.setCpf(request.cpf());
        responsible.setRg(request.rg());
        responsible.setDataNascimento(request.dataNascimento());
        responsible.setTelefone(request.telefone());
        return responsible;
    }
}
