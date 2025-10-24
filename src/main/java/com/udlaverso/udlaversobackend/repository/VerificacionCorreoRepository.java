package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.VerificacionCorreo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VerificacionCorreoRepository extends JpaRepository<VerificacionCorreo, Integer> {
    Optional<VerificacionCorreo> findTopByCorreoVerificacionCorreoAndTipoVerificacionCorreoOrderByExpiracionVerificacionCorreoDesc(String correo, String tipo);
}
