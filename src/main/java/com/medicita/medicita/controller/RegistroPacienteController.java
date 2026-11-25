package com.medicita.medicita.controller;

import com.medicita.medicita.dto.PacienteResponse;
import com.medicita.medicita.dto.RegistroPacienteRequest;
import com.medicita.medicita.service.RegistroPacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegistroPacienteController {

    private final RegistroPacienteService registroPacienteService;

    public RegistroPacienteController(RegistroPacienteService registroPacienteService) {
        this.registroPacienteService = registroPacienteService;
    }

    @PostMapping("/registro")
    public ResponseEntity<PacienteResponse> registrar(
            @Valid @RequestBody RegistroPacienteRequest request) {
        PacienteResponse respuesta = registroPacienteService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}