package com.medicita.medicita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 


public class CitaResponse {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String estado;
    private Long idMedico;
    private String nombreMedico;
    private String especialidad;
    
}
