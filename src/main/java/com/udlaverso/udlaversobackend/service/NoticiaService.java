package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticiaService {
    Page<NoticiaDTO> listar(String q, Pageable pageable);

    List<NoticiaDTO> listarRecientes();

    NoticiaDTO obtenerPorId(Integer id);

    NoticiaDTO obtenerPorTitulo(String titulo);

    NoticiaDTO crear(NoticiaDTO dto);

    NoticiaDTO actualizar(Integer id, NoticiaDTO dto);

    void eliminar(Integer id);
}
