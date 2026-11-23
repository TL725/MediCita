package com.medicita.medicita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PacienteResponse {

    private Long id;
    private String nombre;
    private String correo;
    private String nombreUsuario;

    
}