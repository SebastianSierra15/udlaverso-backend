package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticiaService {
    Page<NoticiaDTO> listar(String q, Pageable pageable);

    List<NoticiaDTO> listarRecientes();

    NoticiaDTO obtenerPorId(Integer id);

    NoticiaDTO obtenerPorTitulo(String titulo);

    NoticiaDTO crear(NoticiaDTO dto);

    NoticiaDTO crearConImagen(NoticiaDTO dto, MultipartFile imagen);

    NoticiaDTO actualizar(Integer id, NoticiaDTO dto);

    NoticiaDTO actualizarConImagen(Integer id, NoticiaDTO dto, MultipartFile imagen);

    void eliminar(Integer id);

    void eliminarNoticia(Integer id);
}
