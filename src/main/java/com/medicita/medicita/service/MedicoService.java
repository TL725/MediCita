package com.medicita.medicita.service;

import com.medicita.medicita.dto.MedicoRequest;
import com.medicita.medicita.dto.MedicoResponse;
import com.medicita.medicita.entity.Especialidad;
import com.medicita.medicita.entity.Medico;
import com.medicita.medicita.entity.Persona;
import com.medicita.medicita.entity.Rol;
import com.medicita.medicita.entity.Usuario;
import com.medicita.medicita.exception.ConflictoException;
import com.medicita.medicita.repository.EspecialidadRepository;
import com.medicita.medicita.repository.MedicoRepository;
import com.medicita.medicita.repository.PersonaRepository;
import com.medicita.medicita.repository.RolRepository;
import com.medicita.medicita.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final RolRepository rolRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(PersonaRepository personaRepository,
                         UsuarioRepository usuarioRepository,
                         MedicoRepository medicoRepository,
                         RolRepository rolRepository,
                         EspecialidadRepository especialidadRepository,
                         PasswordEncoder passwordEncoder) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
        this.medicoRepository = medicoRepository;
        this.rolRepository = rolRepository;
        this.especialidadRepository = especialidadRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MedicoResponse crear(MedicoRequest request) {

        // PASO 1: revisar repetidos
        if (personaRepository.existsByCorreo(request.correo())) {
            throw new ConflictoException("El correo ya está registrado");
        }
        if (personaRepository.existsByNumeroDocumento(request.numeroDocumento())) {
            throw new ConflictoException("El número de documento ya está registrado");
        }
        if (usuarioRepository.existsByNombreUsuario(request.nombreUsuario())) {
            throw new ConflictoException("El nombre de usuario ya está en uso");
        }

        // PASO 2: buscar la especialidad y el rol MEDICO
        Especialidad especialidad = especialidadRepository.findById(request.idEspecialidad())
                .orElseThrow(() -> new ConflictoException("La especialidad no existe"));
        Rol rolMedico = rolRepository.findByNombreRol("MEDICO")
                .orElseThrow(() -> new IllegalStateException(
                        "Error de configuración: no existe el rol MEDICO"));

        // PASO 3: guardar la Persona
        Persona persona = new Persona();
        persona.setNombre(request.nombre());
        persona.setTipoDocumento(request.tipoDocumento());
        persona.setNumeroDocumento(request.numeroDocumento());
        persona.setCorreo(request.correo());
        persona.setCelular(request.celular());
        persona = personaRepository.save(persona);

        // PASO 4: guardar el Usuario (contraseña cifrada)
        Usuario usuario = new Usuario();
        usuario.setPersona(persona);
        usuario.setRol(rolMedico);
        usuario.setNombreUsuario(request.nombreUsuario());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuarioRepository.save(usuario);

        // PASO 5: guardar el Medico
        Medico medico = new Medico();
        medico.setPersona(persona);
        medico.setEspecialidad(especialidad);
        medico.setTarjetaProfesional(request.tarjetaProfesional());
        medico = medicoRepository.save(medico);

        return new MedicoResponse(medico.getId(), persona.getNombre(), persona.getCorreo(),
                usuario.getNombreUsuario(), medico.getTarjetaProfesional(), especialidad.getNombre());
    }

    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        return medicoRepository.findAll().stream()
                .map(m -> new MedicoResponse(
                        m.getId(),
                        m.getPersona().getNombre(),
                        m.getPersona().getCorreo(),
                        null,
                        m.getTarjetaProfesional(),
                        m.getEspecialidad().getNombre()))
                .toList();
    }
}