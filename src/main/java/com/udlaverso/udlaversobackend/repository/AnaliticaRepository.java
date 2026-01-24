package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Analitica;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import com.udlaverso.udlaversobackend.entity.TipoAnalitica;
import com.udlaverso.udlaversobackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AnaliticaRepository extends JpaRepository<Analitica, Integer> {
    boolean existsByUsuarioAnaliticaAndProyectoAnaliticaAndTipoAnaliticaAndFechaeventoAnaliticaAfter(
            Usuario usuario,
            Proyecto proyecto,
            TipoAnalitica tipo,
            LocalDateTime fechaLimite
    );
}
