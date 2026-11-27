package com.medicita.medicita.controller;

import com.medicita.medicita.dto.AgendarCitaRequest;
import com.medicita.medicita.dto.CitaResponse;
import com.medicita.medicita.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<CitaResponse> agendar(
            @Valid @RequestBody AgendarCitaRequest request,
            Authentication authentication) {
        CitaResponse respuesta = citaService.agendar(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}