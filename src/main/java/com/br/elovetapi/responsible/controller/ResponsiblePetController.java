package com.br.elovetapi.responsible.controller;

import com.br.elovetapi.responsible.dtos.ResponsiblePetRequestDTO;
import com.br.elovetapi.responsible.dtos.ResponsiblePetResponseDTO;
import com.br.elovetapi.responsible.service.ResponsiblePetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("responsible/pets")
public class ResponsiblePetController {
    private final ResponsiblePetService responsiblePetService;

    public ResponsiblePetController(ResponsiblePetService responsiblePetService) {
        this.responsiblePetService = responsiblePetService;
    }

    @GetMapping("/{idResponsavel}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public List<ResponsiblePetResponseDTO> getPetsByResponsible(@PathVariable Long idResponsavel) {
        return responsiblePetService.getPetsByResponsible(idResponsavel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESPONSAVEL')")
    public ResponsiblePetResponseDTO linkPetToResponsible(@Valid @RequestBody ResponsiblePetRequestDTO responsiblePet) {
        return responsiblePetService.linkPetToResponsible(responsiblePet);
    }

    @DeleteMapping("/{idPetResponsavel}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESPONSAVEL')")
    public void unlinkPet(@PathVariable Long idPetResponsavel) {
        responsiblePetService.unlinkPet(idPetResponsavel);
    }
}
