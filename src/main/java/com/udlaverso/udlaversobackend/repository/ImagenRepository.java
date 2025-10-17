package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagenRepository extends JpaRepository<Imagen, Integer> {
    List<Imagen> findByProyecto_IdProyecto(Integer idProyecto);
}
