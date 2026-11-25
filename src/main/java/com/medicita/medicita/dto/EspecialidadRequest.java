package com.medicita.medicita.dto;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadRequest(@NotBlank String nombre) {
}