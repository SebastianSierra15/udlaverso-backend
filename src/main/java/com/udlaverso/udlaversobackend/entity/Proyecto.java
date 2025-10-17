package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyecto")
@Getter
@Setter
@NoArgsConstructor
public class Proyecto {
    @Id
    @Column(name = "id_proyecto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProyecto;

    @Column(name = "nombre_proyecto", length = 255, nullable = false)
    private String nombreProyecto;

    @Column(name = "fechacreacion_proyecto")
    private LocalDate fechacreacionProyecto;

    @Column(name = "descripcioncorta_proyecto", length = 500)
    private String descripcioncortaProyecto;

    @Column(name = "descripcionlarga_proyecto", length = 2500)
    private String descripcionlargaProyecto;

    @Column(name = "objetivo_proyecto", length = 255)
    private String objetivoProyecto;

    @Column(name = "video_proyecto", length = 255)
    private String videoProyecto;

    @Column(name = "autor_proyecto", length = 150)
    private String autorProyecto;

    @Column(name = "visualizaciones_proyecto")
    private Integer visualizacionesProyecto;

    @Column(name = "estado_proyecto")
    private Byte estadoProyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_categoria")
    private Categoria categoriaProyecto;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenesProyecto = new ArrayList<>();
}