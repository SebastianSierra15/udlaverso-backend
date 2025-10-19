package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    Page<Proyecto> findByCategoriaProyecto_NombreCategoriaIgnoreCase(String nombre, Pageable pageable);

    Page<Proyecto> findByNombreProyectoContainingIgnoreCase(String q, Pageable pageable);

    @Query("SELECT p FROM Proyecto p WHERE p.estadoProyecto = 1 ORDER BY p.visualizacionesProyecto DESC")
    List<Proyecto> findTopProyectosMasVistos(Pageable pageable);
}