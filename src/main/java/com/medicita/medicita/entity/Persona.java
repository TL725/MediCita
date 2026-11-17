package com.medicita.medicita.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "personas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true)
    private String numeroDocumento;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String celular;
}