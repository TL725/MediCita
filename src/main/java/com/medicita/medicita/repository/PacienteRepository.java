package com.medicita.medicita.repository;

import com.medicita.medicita.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByPersonaId(Long idPersona);
}