package com.medicita.medicita.service;

import com.medicita.medicita.entity.Cita;
import com.medicita.medicita.entity.EstadoCita;
import com.medicita.medicita.entity.Paciente;
import com.medicita.medicita.entity.Usuario;
import com.medicita.medicita.repository.CitaRepository;
import com.medicita.medicita.repository.PacienteRepository;
import com.medicita.medicita.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public Cita cancelar(Long idCita, String nombreUsuario, boolean esAdmin) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La cita no existe"));

        if (!esAdmin) {
            Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

            Paciente paciente = pacienteRepository
                    .findByPersonaId(usuario.getPersona().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.FORBIDDEN, "No eres un paciente"));

            if (!cita.getPaciente().getId().equals(paciente.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "Solo puedes cancelar tus propias citas");
            }
        }

        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Solo se pueden cancelar citas pendientes");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        return citaRepository.save(cita);
    }
}