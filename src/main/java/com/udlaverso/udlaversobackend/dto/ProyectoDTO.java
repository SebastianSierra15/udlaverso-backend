package com.udlaverso.udlaversobackend.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProyectoDTO {
    private Integer id;
    private String nombre;
    private LocalDate fechaCreacion;
    private String descripcionCorta;
    private String descripcionLarga;
    private String objetivo;
    private String video;
    private String autor;
    private Integer visualizaciones;
    private Byte estado;
    private Integer categoriaId;
    private String categoriaNombre;
    private List<String> imagenes; // solo rutas
}