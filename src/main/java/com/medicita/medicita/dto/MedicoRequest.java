package com.medicita.medicita.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicoRequest(
        @NotBlank String nombre,
        @NotBlank String tipoDocumento,
        @NotBlank String numeroDocumento,
        @NotBlank String correo,
        @NotBlank String celular,
        @NotBlank String nombreUsuario,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String tarjetaProfesional,
        @NotNull Long idEspecialidad) {
}