package com.medicita.medicita.repository;

import com.medicita.medicita.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByEspecialidadId(Long idEspecialidad);
    Optional<Medico> findByPersonaId(Long idPersona);
}