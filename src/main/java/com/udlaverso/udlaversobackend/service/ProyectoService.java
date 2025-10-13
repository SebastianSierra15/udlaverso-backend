package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import org.springframework.data.domain.*;

public interface ProyectoService {
    ProyectoDTO crear(ProyectoDTO dto);

    ProyectoDTO obtener(Integer id);

    Page<ProyectoDTO> listar(String q, String categoria, Pageable pageable);

    ProyectoDTO actualizar(Integer id, ProyectoDTO dto);

    void eliminar(Integer id);
}