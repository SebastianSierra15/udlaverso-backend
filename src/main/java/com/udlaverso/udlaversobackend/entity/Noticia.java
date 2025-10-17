package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "noticia")
@Getter
@Setter
@NoArgsConstructor
public class Noticia {
    @Id
    @Column(name = "id_noticia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNoticia;

    @Column(name = "titulo_noticia", length = 255)
    private String tituloNoticia;

    @Column(name = "contenido_noticia", length = 1500)
    private String contenidoNoticia;

    @Column(name = "fechapublicacion_noticia")
    private LocalDateTime fechapublicacionNoticia;

    @Column(name = "imagen_noticia", length = 255)
    private String imagenNoticia;

    @Column(name = "estado_noticia")
    private Byte estadoNoticia;
}