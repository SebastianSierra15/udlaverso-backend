package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    Page<Proyecto> findByCategoria_NombreIgnoreCase(String nombre, Pageable pageable);

    Page<Proyecto> findByNombreContainingIgnoreCase(String q, Pageable pageable);
}