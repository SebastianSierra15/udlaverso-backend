package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analitica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analitica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAnalitica;

    @Column(name = "descripcion_analitica", length = 255)
    private String descripcionAnalitica;

    @Column(name = "ipusuario_analitica", length = 100)
    private String ipusuarioAnalitica;

    @Column(name = "useragent_analitica", length = 255)
    private String useragentAnalitica;

    @Column(name = "fechaevento_analitica", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaeventoAnalitica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuarioAnalitica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_tipoanalitica")
    private TipoAnalitica tipoAnalitica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_proyecto")
    private Proyecto proyectoAnalitica;
}
