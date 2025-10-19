package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;

import java.util.List;

public interface NoticiaService {
    List<NoticiaDTO> listar();

    NoticiaDTO obtenerPorId(Integer id);

    NoticiaDTO obtenerPorTitulo(String titulo);

    NoticiaDTO crear(NoticiaDTO dto);

    NoticiaDTO actualizar(Integer id, NoticiaDTO dto);

    void eliminar(Integer id);
}
