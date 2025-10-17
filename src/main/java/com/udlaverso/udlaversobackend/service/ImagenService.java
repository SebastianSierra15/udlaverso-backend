package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.ImagenDTO;

import java.util.List;

public interface ImagenService {
    List<ImagenDTO> listarPorProyecto(Integer idProyecto);

    ImagenDTO guardar(ImagenDTO dto, Integer idProyecto);

    void eliminar(Integer idImagen);
}
