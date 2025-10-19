package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticiaRepository extends JpaRepository<Noticia, Integer> {
    boolean existsByTituloNoticia(String tituloNoticia);

    Optional<Noticia> findByTituloNoticia(String tituloNoticia);
}
