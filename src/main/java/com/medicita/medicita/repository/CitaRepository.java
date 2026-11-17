package com.medicita.medicita.repository;

import com.medicita.medicita.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    boolean existsByMedicoIdAndFechaAndHora(Long idMedico, LocalDate fecha, LocalTime hora);
    boolean existsByPacienteIdAndFechaAndHora(Long idPaciente, LocalDate fecha, LocalTime hora);
    List<Cita> findByPacienteId(Long idPaciente);
    List<Cita> findByMedicoId(Long idMedico);
}