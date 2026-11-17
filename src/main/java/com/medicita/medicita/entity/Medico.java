package com.medicita.medicita.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_persona", unique = true)
    private Persona persona;

    @Column(name = "tarjeta_profesional", nullable = false)
    private String tarjetaProfesional;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;
}