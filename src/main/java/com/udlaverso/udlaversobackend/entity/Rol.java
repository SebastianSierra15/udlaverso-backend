package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre_rol", length = 20, nullable = false, unique = true)
    private String nombreRol;

    @Column(name = "estado_rol")
    private Byte estadoRol;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "permisorol",
            joinColumns = @JoinColumn(name = "fk_id_rol"),
            inverseJoinColumns = @JoinColumn(name = "fk_id_permiso")
    )
    private Set<Permiso> permisosRol;
}
