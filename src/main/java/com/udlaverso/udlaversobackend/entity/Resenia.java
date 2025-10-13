package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resenia")
@Getter
@Setter
@NoArgsConstructor
public class Resenia {
    @Id
    @Column(name = "id_resenia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "valoracion_resenia")
    private Integer valoracion;
    @Column(name = "comentario_resenia", length = 1000)
    private String comentario;
    @Column(name = "estado_resenia")
    private Byte estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_proyecto")
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;
}