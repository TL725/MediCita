package com.medicita.medicita.controller;

import com.medicita.medicita.dto.EspecialidadRequest;
import com.medicita.medicita.entity.Especialidad;
import com.medicita.medicita.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/especialidades")
public class EspecialidadController {

    private final EspecialidadService service;

    public EspecialidadController(EspecialidadService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Especialidad crear(@Valid @RequestBody EspecialidadRequest request) {
        return service.crear(request.nombre());
    }

    @GetMapping
    public List<Especialidad> listar() {
        return service.listar();
    }
}