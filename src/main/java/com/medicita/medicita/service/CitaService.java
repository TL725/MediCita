package com.medicita.medicita.service;

import com.medicita.medicita.dto.AgendarCitaRequest;
import com.medicita.medicita.dto.CitaResponse;
import com.medicita.medicita.entity.Cita;
import com.medicita.medicita.entity.Medico;
import com.medicita.medicita.entity.Paciente;
import com.medicita.medicita.entity.Usuario;
import com.medicita.medicita.exception.AccesoDenegadoException;
import com.medicita.medicita.exception.ConflictoException;
import com.medicita.medicita.exception.RecursoNoEncontradoException;
import com.medicita.medicita.exception.SolicitudInvalidaException;
import com.medicita.medicita.repository.CitaRepository;
import com.medicita.medicita.repository.MedicoRepository;
import com.medicita.medicita.repository.PacienteRepository;
import com.medicita.medicita.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public CitaResponse agendar(String nombreUsuario, AgendarCitaRequest request) {

        // 1) ¿Quién es? (sale del JWT, nunca del JSON)
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new AccesoDenegadoException("Usuario no válido"));

        // 2) ¿Es paciente? Un admin no tiene fila en 'pacientes'
        Paciente paciente = pacienteRepository.findByPersonaId(usuario.getPersona().getId())
                .orElseThrow(() -> new AccesoDenegadoException("Solo los pacientes pueden agendar citas"));

        // 3) ¿Existe el médico?
        Medico medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new RecursoNoEncontradoException("El médico no existe"));

        // 4) La fecha y la hora juntas deben ser futuras
        LocalDateTime momento = LocalDateTime.of(request.getFecha(), request.getHora());
        if (!momento.isAfter(LocalDateTime.now())) {
            throw new SolicitudInvalidaException("La fecha y hora de la cita deben ser futuras");
        }

        // 5) ¿El médico está libre?
        if (citaRepository.existsByMedicoIdAndFechaAndHora(
                medico.getId(), request.getFecha(), request.getHora())) {
            throw new ConflictoException("El médico ya tiene una cita a esa hora");
        }

        // 6) ¿El paciente está libre?
        if (citaRepository.existsByPacienteIdAndFechaAndHora(
                paciente.getId(), request.getFecha(), request.getHora())) {
            throw new ConflictoException("Ya tienes una cita a esa hora");
        }

        // 7) Armar y guardar (el estado nace PENDIENTE en la entidad)
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFecha(request.getFecha());
        cita.setHora(request.getHora());
        cita.setMotivo(request.getMotivo());
        Cita guardada = citaRepository.save(cita);

        // 8) Devolver solo lo que el cliente necesita ver
        return new CitaResponse(
                guardada.getId(),
                guardada.getFecha(),
                guardada.getHora(),
                guardada.getMotivo(),
                guardada.getEstado().name(),
                medico.getId(),
                medico.getPersona().getNombre(),
                medico.getEspecialidad().getNombre());
    }
}