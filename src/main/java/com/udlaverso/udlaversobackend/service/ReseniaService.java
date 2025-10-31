package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.ReseniaDTO;

import java.util.List;

public interface ReseniaService {

    ReseniaDTO obtenerReseniaPorId(Integer idResenia);

    List<ReseniaDTO> listarReseniasPorProyecto(Integer proyectoId);

    ReseniaDTO crearResenia(ReseniaDTO reseniaDTO, Integer proyectoId, Integer usuarioId);

    void eliminarResenia(Integer idResenia);

    ReseniaDTO actualizarResenia(Integer idResenia, ReseniaDTO reseniaDTO);
}
