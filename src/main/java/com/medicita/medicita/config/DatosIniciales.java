package com.medicita.medicita.config;

import com.medicita.medicita.entity.Persona;
import com.medicita.medicita.entity.Rol;
import com.medicita.medicita.entity.Usuario;
import com.medicita.medicita.repository.PersonaRepository;
import com.medicita.medicita.repository.RolRepository;
import com.medicita.medicita.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    public DatosIniciales(RolRepository rolRepository,
                          PersonaRepository personaRepository,
                          UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        // 1. Roles: se crean solo si no existen
        for (String nombre : List.of("ADMIN", "MEDICO", "PACIENTE")) {
            boolean existe = rolRepository.findAll().stream()
                    .anyMatch(r -> nombre.equals(r.getNombreRol()));
            if (!existe) {
                Rol rol = new Rol();
                rol.setNombreRol(nombre);
                rolRepository.save(rol);
                System.out.println("Rol creado: " + nombre);
            }
        }

        // 2. Administrador: se crea solo si no existe
        boolean adminExiste = usuarioRepository.findAll().stream()
                .anyMatch(u -> "admin".equals(u.getNombreUsuario()));
        if (adminExiste) {
            return;
        }
        if (adminPassword.isBlank()) {
            System.out.println("AVISO: falta la variable ADMIN_PASSWORD. No se creo el administrador.");
            return;
        }

        Persona persona = new Persona();
        persona.setNombre("Administrador MediCita");
        persona.setCorreo("admin@medicita.com");
        persona.setTipoDocumento("CC");
        persona.setNumeroDocumento("0000000001");
        persona.setCelular("3000000000");
        personaRepository.save(persona);

        Rol rolAdmin = rolRepository.findAll().stream()
                .filter(r -> "ADMIN".equals(r.getNombreRol()))
                .findFirst().orElseThrow();

        Usuario admin = new Usuario();
        admin.setNombreUsuario("admin");
        admin.setPassword(new BCryptPasswordEncoder().encode(adminPassword));
        admin.setPersona(persona);
        admin.setRol(rolAdmin);
        usuarioRepository.save(admin);

        System.out.println("Administrador creado: usuario 'admin'");
    }
}