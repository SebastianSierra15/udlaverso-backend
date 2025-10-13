package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "correo_usuario", length = 150, unique = true, nullable = false)
    private String correo;

    @Column(name = "contrasenia_usuario", length = 100, nullable = false)
    private String contrasenia;

    @Column(name = "nombres_usuario", length = 150)
    private String nombres;
    @Column(name = "apellidos_usuario", length = 150)
    private String apellidos;
    @Column(name = "universidad_usuario", length = 100)
    private String universidad;
    @Column(name = "estado_usuario")
    private Byte estado;

    @Column(name = "fechacreacion_usuario")
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_rol")
    private Rol rol;
}