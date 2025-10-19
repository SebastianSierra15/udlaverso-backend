package com.udlaverso.udlaversobackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticiaDTO {
    private Integer idNoticia;
    private String tituloNoticia;
    private String contenidoNoticia;
    private LocalDateTime fechapublicacionNoticia;
    private String autorNoticia;
    private String ImagenNoticia;
    private Byte estadoNoticia;
}
