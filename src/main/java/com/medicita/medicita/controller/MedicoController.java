package com.medicita.medicita.controller;

import com.medicita.medicita.dto.MedicoRequest;
import com.medicita.medicita.dto.MedicoResponse;
import com.medicita.medicita.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicoResponse crear(@Valid @RequestBody MedicoRequest request) {
        return service.crear(request);
    }

    @GetMapping
    public List<MedicoResponse> listar() {
        return service.listar();
    }
}