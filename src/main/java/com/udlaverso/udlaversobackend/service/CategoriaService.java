package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {
    List<CategoriaDTO> listar();
    CategoriaDTO obtenerPorId(Integer id);
    CategoriaDTO crear(CategoriaDTO dto);
    CategoriaDTO actualizar(Integer id, CategoriaDTO dto);
    void eliminar(Integer id);
}
