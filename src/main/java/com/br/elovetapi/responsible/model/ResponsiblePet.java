package com.br.elovetapi.responsible.model;

import com.br.elovetapi.pet.model.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "elo_pet_responsavel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsiblePet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPetResponsavel;

    @ManyToOne
    @JoinColumn(name = "id_pet")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "id_responsavel")
    private Responsible responsavel;

    private LocalDateTime dataElo;
}
