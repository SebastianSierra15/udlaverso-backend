package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.TipoAnalitica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoAnaliticaRepository extends JpaRepository<TipoAnalitica, Integer> {

}
