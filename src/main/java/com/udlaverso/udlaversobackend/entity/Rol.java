package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
public class Rol {
    @Id
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre_rol", length = 20)
    private String nombreRol;

    @Column(name = "estado_rol")
    private Byte estadoRol;
}
