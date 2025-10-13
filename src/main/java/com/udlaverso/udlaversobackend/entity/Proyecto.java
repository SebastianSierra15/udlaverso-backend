package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "proyecto")
@Getter
@Setter
@NoArgsConstructor
public class Proyecto {
    @Id
    @Column(name = "id_proyecto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_proyecto", length = 255, nullable = false)
    private String nombre;

    @Column(name = "fechacreacion_proyecto")
    private LocalDate fechaCreacion;

    @Column(name = "descripcioncorta_proyecto", length = 500)
    private String descripcionCorta;

    @Column(name = "descripcionlarga_proyecto", length = 2500)
    private String descripcionLarga;

    @Column(name = "objetivo_proyecto", length = 255)
    private String objetivo;

    @Column(name = "video_proyecto", length = 255)
    private String video;

    @Column(name = "autor_proyecto", length = 150)
    private String autor;

    @Column(name = "visualizaciones_proyecto")
    private Integer visualizaciones;

    @Column(name = "estado_proyecto")
    private Byte estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_categoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArrayList<Imagen> imagenes = new ArrayList<>();
}