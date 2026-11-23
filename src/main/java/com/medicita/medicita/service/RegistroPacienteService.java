package com.medicita.medicita.service;

import com.medicita.medicita.dto.PacienteResponse;
import com.medicita.medicita.dto.RegistroPacienteRequest;
import com.medicita.medicita.entity.Paciente;
import com.medicita.medicita.entity.Persona;
import com.medicita.medicita.entity.Rol;
import com.medicita.medicita.entity.Usuario;
import com.medicita.medicita.exception.ConflictoException;
import com.medicita.medicita.repository.PacienteRepository;
import com.medicita.medicita.repository.PersonaRepository;
import com.medicita.medicita.repository.RolRepository;
import com.medicita.medicita.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroPacienteService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroPacienteService(PersonaRepository personaRepository,
                                   UsuarioRepository usuarioRepository,
                                   PacienteRepository pacienteRepository,
                                   RolRepository rolRepository, 
                                   PasswordEncoder passwordEncoder) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

        @Transactional
    public PacienteResponse registrar(RegistroPacienteRequest request) {

        // PASO 1: revisar repetidos
        if (personaRepository.existsByCorreo(request.getCorreo())) {
            throw new ConflictoException("El correo ya está registrado");
        }
        if (personaRepository.existsByNumeroDocumento(request.getNumeroDocumento())) {
            throw new ConflictoException("El número de documento ya está registrado");
        }
        if (usuarioRepository.existsByNombreUsuario(request.getNombreUsuario())) {
            throw new ConflictoException("El nombre de usuario ya está en uso");
        }

                // PASO 2: buscar el rol PACIENTE (lo decide el Service, no el usuario)
        Rol rolPaciente = rolRepository.findByNombreRol("PACIENTE")
                .orElseThrow(() -> new IllegalStateException(
                        "Error de configuración: no existe el rol PACIENTE"));

        // PASO 3: guardar la Persona
        Persona persona = new Persona();
        persona.setNombre(request.getNombre());
        persona.setTipoDocumento(request.getTipoDocumento());
        persona.setNumeroDocumento(request.getNumeroDocumento());
        persona.setCorreo(request.getCorreo());
        persona.setCelular(request.getCelular());
        persona = personaRepository.save(persona);

        // PASO 4: guardar el Usuario (contraseña encriptada)
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setRol(rolPaciente);
        usuario.setNombreUsuario(request.getNombreUsuario());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario = usuarioRepository.save(usuario);

        // PASO 5: guardar el Paciente
        Paciente paciente = new Paciente();
        paciente.setPersona(persona);
        paciente.setFechaNacimiento(request.getFechaNacimiento());
        paciente.setDireccion(request.getDireccion());
        paciente.setGenero(request.getGenero());
        paciente = pacienteRepository.save(paciente);

        // PASO 6: devolver la respuesta (sin password)
        return new PacienteResponse(
                paciente.getId(),
                persona.getNombre(),
                persona.getCorreo(),
                usuario.getNombreUsuario());
    }
}