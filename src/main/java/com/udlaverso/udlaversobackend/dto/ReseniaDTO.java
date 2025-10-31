package com.udlaverso.udlaversobackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReseniaDTO {
    private Integer idResenia;
    private Integer valoracionResenia;
    private String comentarioResenia;
    private LocalDateTime fechaResenia;
    private Integer usuarioId;
    private String usuarioNombres;
    private String usuarioApellidos;
}
