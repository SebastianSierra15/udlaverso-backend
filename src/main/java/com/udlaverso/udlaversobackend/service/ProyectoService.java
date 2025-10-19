package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import org.springframework.data.domain.*;

import java.util.List;

public interface ProyectoService {
    ProyectoDTO crear(ProyectoDTO dto);

    ProyectoDTO obtener(Integer id);

    ProyectoDTO obtenerPorNombre(String nombre);

    Page<ProyectoDTO> listar(String q, String categoria, Pageable pageable);

    List<ProyectoDTO> listarMasVistos(int limite);

    ProyectoDTO actualizar(Integer id, ProyectoDTO dto);

    void eliminar(Integer id);
}