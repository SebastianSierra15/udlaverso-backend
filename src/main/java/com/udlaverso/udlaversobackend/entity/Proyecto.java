package com.udlaverso.udlaversobackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "descripcioncorta_proyecto", length = 1000)
    private String descripcioncortaProyecto;

    @Column(name = "descripcionlarga_proyecto", length = 3500)
    private String descripcionlargaProyecto;

    @Column(name = "objetivo_proyecto", length = 500)
    private String objetivoProyecto;

    @Column(name = "herramientas_proyecto", length = 255)
    private String herramientasProyecto;

    @Column(name = "palabrasclave_proyecto", length = 255)
    private String palabrasclaveProyecto;

    @Column(name = "video_proyecto", length = 255)
    private String videoProyecto;

    @Column(name = "autor_proyecto", length = 200)
    private String autorProyecto;

    @Column(name = "visualizaciones_proyecto")
    private Integer visualizacionesProyecto;

    @Column(name = "estado_proyecto")
    private Byte estadoProyecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_id_categoria")
    private Categoria categoriaProyecto;

    @OneToMany(mappedBy = "proyectoImagen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenesProyecto = new ArrayList<>();

    @OneToMany(mappedBy = "proyectoResenia", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Resenia> reseniasProyecto = new ArrayList<>();
}