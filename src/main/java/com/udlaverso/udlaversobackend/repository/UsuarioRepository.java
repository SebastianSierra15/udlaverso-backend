package com.udlaverso.udlaversobackend.repository;

import com.udlaverso.udlaversobackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoUsuario(String correoUsuario);

    boolean existsByCorreoUsuario(String correoUsuario);

    @Query("SELECT u FROM Usuario u " +
            "JOIN FETCH u.rolUsuario r " +
            "JOIN FETCH r.permisosRol " +
            "WHERE u.correoUsuario = :correo")
    Optional<Usuario> findByCorreoUsuarioConPermisos(@Param("correo") String correo);
}