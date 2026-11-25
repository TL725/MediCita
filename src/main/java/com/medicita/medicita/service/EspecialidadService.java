package com.medicita.medicita.service;

import com.medicita.medicita.entity.Especialidad;
import com.medicita.medicita.exception.ConflictoException;
import com.medicita.medicita.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository repository;

    public EspecialidadService(EspecialidadRepository repository) {
        this.repository = repository;
    }

    public Especialidad crear(String nombre) {
        String limpio = nombre.trim();
        boolean existe = repository.findAll().stream()
                .anyMatch(e -> e.getNombre().equalsIgnoreCase(limpio));
        if (existe) {
            throw new ConflictoException("La especialidad ya existe");
        }
        Especialidad e = new Especialidad();
        e.setNombre(limpio);
        return repository.save(e);
    }

    public List<Especialidad> listar() {
        return repository.findAll();
    }
}