package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    Page<Proyecto> findByCategoriaProyecto_NombreCategoriaIgnoreCase(String nombre, Pageable pageable);

    Page<Proyecto> findByNombreProyectoContainingIgnoreCase(String q, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Proyecto p " +
            "LEFT JOIN FETCH p.reseniasProyecto r " +
            "LEFT JOIN FETCH r.usuarioResenia u " +
            "WHERE REPLACE(LOWER(p.nombreProyecto), ' ', '-') = LOWER(:slug)")
    Optional<Proyecto> findBySlugConResenias(@Param("slug") String slug);

    @Query("SELECT p FROM Proyecto p WHERE p.estadoProyecto = 1 ORDER BY p.visualizacionesProyecto DESC")
    List<Proyecto> findTopProyectosMasVistos(Pageable pageable);

    boolean existsByNombreProyectoIgnoreCase(String nombreProyecto);

    boolean existsByNombreProyectoIgnoreCaseAndIdProyectoNot(String nombreProyecto, Integer idProyecto);

    Page<Proyecto> findByEstadoProyectoNot(byte estado, Pageable pageable);

    Page<Proyecto> findByCategoriaProyecto_NombreCategoriaIgnoreCaseAndEstadoProyectoNot(
            String categoria,
            byte estado,
            Pageable pageable);

    Page<Proyecto> findByNombreProyectoContainingIgnoreCaseAndEstadoProyectoNot(
            String q,
            byte estado,
            Pageable pageable);
}