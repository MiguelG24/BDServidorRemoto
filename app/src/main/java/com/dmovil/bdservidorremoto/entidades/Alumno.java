package com.dmovil.bdservidorremoto.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alumno {

    private String control;
    private String nombre;
    private String apellidos;
    private String carrera;
    private String telefono;
    private String email;
    private String direccion;
}
