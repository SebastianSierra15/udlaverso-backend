package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipoanalitica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAnalitica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipoanalitica")
    private Integer idTipoAnalitica;

    @Column(name = "nombre_tipoanalitica", length = 50, nullable = false)
    private String nombreTipoAnalitica;
}
