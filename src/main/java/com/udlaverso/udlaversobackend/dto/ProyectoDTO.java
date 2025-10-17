package com.udlaverso.udlaversobackend.dto;

import com.udlaverso.udlaversobackend.dto.ImagenDTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProyectoDTO {
    private Integer idProyecto;
    private String nombreProyecto;
    private LocalDate fechacreacionProyecto;
    private String descripcioncortaProyecto;
    private String descripcionlargaProyecto;
    private String objetivoProyecto;
    private String videoProyecto;
    private String autorProyecto;
    private Integer visualizacionesProyecto;
    private Byte estadoProyecto;
    private List<ImagenDTO> imagenesProyecto;
    private Integer categoriaId;
    private String categoriaNombre;
}