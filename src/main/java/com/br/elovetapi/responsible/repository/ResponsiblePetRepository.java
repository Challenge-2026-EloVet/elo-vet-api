package com.br.elovetapi.responsible.repository;

import com.br.elovetapi.responsible.model.ResponsiblePet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponsiblePetRepository extends JpaRepository<ResponsiblePet, Long> {
    List<ResponsiblePet> findByResponsavelIdResponsavel(Long idResponsavel);

    boolean existsByPetIdPetAndResponsavelIdResponsavel(Long idPet, Long idResponsavel);
}
