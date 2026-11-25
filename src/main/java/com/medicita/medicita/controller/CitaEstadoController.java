package com.medicita.medicita.controller;

import com.medicita.medicita.dto.CitaEstadoResponse;
import com.medicita.medicita.entity.Cita;
import com.medicita.medicita.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaEstadoController {

    private final CitaService citaService;

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CitaEstadoResponse> cancelar(@PathVariable Long id,
                                                       Authentication auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Cita cita = citaService.cancelar(id, auth.getName(), esAdmin);
        return ResponseEntity.ok(new CitaEstadoResponse(cita.getId(), cita.getEstado()));
    }
}