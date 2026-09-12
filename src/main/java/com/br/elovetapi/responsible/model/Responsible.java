package com.br.elovetapi.responsible.model;

import com.br.elovetapi.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "elo_responsavel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Responsible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResponsavel;

    private String nomeCompleto;

    private String cpf;

    private String rg;

    private LocalDate dataNascimento;

    private String telefone;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;
}
