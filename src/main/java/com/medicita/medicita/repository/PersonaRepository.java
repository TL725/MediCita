package com.medicita.medicita.repository;

import com.medicita.medicita.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    boolean existsByNumeroDocumento(String numeroDocumento);
    boolean existsByCorreo(String correo);
}