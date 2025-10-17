package com.udlaverso.udlaversobackend.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class ImagenDTO {
    private Integer idImagen;
    private String rutaImagen;
    private String tipoImagen;
    private Integer proyectoId;
}
