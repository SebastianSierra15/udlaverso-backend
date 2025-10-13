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
    private Integer id;
    @Column(name = "titulo_noticia", length = 255)
    private String titulo;
    @Column(name = "contenido_noticia", length = 1500)
    private String contenido;
    @Column(name = "fechapublicacion_noticia")
    private LocalDateTime fechaPublicacion;
    @Column(name = "imagen_noticia", length = 255)
    private String imagen;
    @Column(name = "estado_noticia")
    private Byte estado;
}