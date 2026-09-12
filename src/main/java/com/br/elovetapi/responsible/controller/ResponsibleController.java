package com.br.elovetapi.responsible.controller;

import com.br.elovetapi.responsible.dtos.ResponsibleRequestDTO;
import com.br.elovetapi.responsible.dtos.ResponsibleResponseDTO;
import com.br.elovetapi.responsible.model.Responsible;
import com.br.elovetapi.responsible.service.ResponsibleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("responsible")
public class ResponsibleController {
    private final ResponsibleService responsibleService;

    public ResponsibleController(ResponsibleService responsibleService) {
        this.responsibleService = responsibleService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public List<Responsible> getAllResponsibles() {
        return responsibleService.getAllResponsibles();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Responsible getResponsibleById(@PathVariable Long id) {
        return responsibleService.getResponsibleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESPONSAVEL')")
    public ResponsibleResponseDTO createResponsible(@Valid @RequestBody ResponsibleRequestDTO responsible) {
        return responsibleService.createResponsible(responsible);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESPONSAVEL')")
    public ResponsibleResponseDTO updateResponsible(@PathVariable Long id, @Valid @RequestBody ResponsibleRequestDTO responsible) {
        return responsibleService.updateResponsible(id, responsible);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESPONSAVEL')")
    public void deleteResponsible(@PathVariable Long id) {
        responsibleService.deleteResponsible(id);
    }
}
