package com.br.elovetapi.veterinary.controller;

import com.br.elovetapi.veterinary.dtos.VeterinaryRequestDTO;
import com.br.elovetapi.veterinary.dtos.VeterinaryResponseDTO;
import com.br.elovetapi.veterinary.model.Veterinary;
import com.br.elovetapi.veterinary.service.VeterinaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("veterinary")
public class VeterinaryController {
    private final VeterinaryService veterinaryService;

    public VeterinaryController(VeterinaryService veterinaryService) {
        this.veterinaryService = veterinaryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Veterinary> getAllVeterinaries() {
        return veterinaryService.getAllVeterinaries();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Veterinary getVeterinaryById(@PathVariable Long id) {
        return veterinaryService.getVeterinaryById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinaryResponseDTO createVeterinary(@Valid @RequestBody VeterinaryRequestDTO veterinary) {
        return veterinaryService.createVeterinary(veterinary);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VeterinaryResponseDTO updateVeterinary(@PathVariable Long id, @Valid @RequestBody VeterinaryRequestDTO veterinary) {
        return veterinaryService.updateVeterinary(id, veterinary);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVeterinary(@PathVariable Long id) {
        veterinaryService.deleteVeterinary(id);
    }
}