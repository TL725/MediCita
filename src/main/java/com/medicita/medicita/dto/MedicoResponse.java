package com.medicita.medicita.dto;

public record MedicoResponse(
        Long id,
        String nombre,
        String correo,
        String nombreUsuario,
        String tarjetaProfesional,
        String especialidad) {
}