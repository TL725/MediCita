package com.medicita.medicita.service;

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

    // Aquí irá el método registrar(...) con @Transactional (paso siguiente)
}