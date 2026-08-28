package com.br.elovetapi.pet.controller;

import com.br.elovetapi.pet.model.Pet;
import com.br.elovetapi.pet.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{eloId")
    @ResponseStatus(HttpStatus.OK)
    public Pet getPetById(@PathVariable Long eloId) {
        return petService.getPetById(eloId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pet createPet(@RequestBody Pet pet){
        return petService.createPet(pet);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Pet updatePet(@RequestBody Pet pet){
        return petService.updatePet(pet);
    }

    @DeleteMapping("/{eloId}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void deletePet(@PathVariable Long eloId){
        petService.deletePet(eloId);
    }
}
