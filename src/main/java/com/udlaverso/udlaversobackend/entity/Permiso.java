package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Permiso {
    @Id
    @Column(name = "id_permiso")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPermiso;

    @Column(name = "nombre_permiso", length = 50)
    private String nombrePermiso;
}
