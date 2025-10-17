package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@Data
public class Usuario {
    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(name = "correo_usuario", length = 150, unique = true, nullable = false)
    private String correoUsuario;

    @Column(name = "contrasenia_usuario", length = 100, nullable = false)
    private String contraseniaUsuario;

    @Column(name = "nombres_usuario", length = 150)
    private String nombresUsuario;

    @Column(name = "apellidos_usuario", length = 150)
    private String apellidosUsuario;

    @Column(name = "universidad_usuario", length = 100)
    private String universidadUsuario;

    @Column(name = "estado_usuario")
    private Byte estadoUsuario;

    @Column(name = "fechacreacion_usuario")
    private LocalDateTime fechacreacionUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_rol")
    private Rol rolUsuario;
}