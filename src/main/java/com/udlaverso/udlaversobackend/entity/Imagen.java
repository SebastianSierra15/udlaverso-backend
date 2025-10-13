package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imagen")
@Getter
@Setter
@NoArgsConstructor
public class Imagen {
    @Id
    @Column(name = "id_imagen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ruta_imagen", length = 255)
    private String ruta;
    @Column(name = "tipo_imagen", columnDefinition = "TEXT")
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_proyecto")
    private Proyecto proyecto;
}