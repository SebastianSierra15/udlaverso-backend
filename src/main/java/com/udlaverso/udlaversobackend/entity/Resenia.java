package com.udlaverso.udlaversobackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resenia")
@Getter
@Setter
@NoArgsConstructor
public class Resenia {
    @Id
    @Column(name = "id_resenia")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResenia;

    @Column(name = "valoracion_resenia")
    private Integer valoracionResenia;

    @Column(name = "comentario_resenia", length = 1000)
    private String comentarioResenia;

    @Column(name = "fecha_resenia", updatable = false, insertable = false)
    private LocalDateTime fechaResenia;

    @Column(name = "estado_resenia")
    private Byte estadoResenia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_proyecto")
    private Proyecto proyectoResenia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_usuario")
    @JsonManagedReference
    private Usuario usuarioResenia;
}