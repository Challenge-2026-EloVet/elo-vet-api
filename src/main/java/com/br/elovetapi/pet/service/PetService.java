package com.br.elovetapi.pet.service;

import com.br.elovetapi.pet.exceptions.PetNotFoundException;
import com.br.elovetapi.pet.model.Pet;
import com.br.elovetapi.pet.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet getPetById(Long eloId) {
        return petRepository.findById(eloId).orElseThrow(() -> new PetNotFoundException("Pet not found with id: " + eloId));
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        Pet existingPet = getPetById(pet.getEloId());
        existingPet.setNome(pet.getNome());
        return petRepository.save(existingPet);
    }

    public void deletePet(Long eloId) {
        petRepository.delete(getPetById(eloId));
    }
}
