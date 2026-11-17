package com.medicita.medicita.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rol")
    private Rol rol;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    // Aquí se guarda el hash BCrypt, nunca la clave real
    @Column(nullable = false)
    private String password;
}