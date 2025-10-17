package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    Page<Proyecto> findByCategoriaProyecto_NombreCategoriaIgnoreCase(String nombre, Pageable pageable);

    Page<Proyecto> findByNombreProyectoContainingIgnoreCase(String q, Pageable pageable);
}