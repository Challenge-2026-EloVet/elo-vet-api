package com.br.elovetapi.responsible.repository;

import com.br.elovetapi.responsible.model.Responsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsibleRepository extends JpaRepository<Responsible, Long> {
    Optional<Responsible> findByUsuarioIdUsuario(Long idUsuario);
}
