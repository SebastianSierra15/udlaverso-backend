package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
