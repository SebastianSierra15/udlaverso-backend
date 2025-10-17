package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.ImagenDTO;
import com.udlaverso.udlaversobackend.entity.Imagen;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import com.udlaverso.udlaversobackend.mapper.ImagenMapper;
import com.udlaverso.udlaversobackend.repository.ImagenRepository;
import com.udlaverso.udlaversobackend.repository.ProyectoRepository;
import com.udlaverso.udlaversobackend.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImagenServiceImpl implements ImagenService {
    private final ImagenRepository imagenRepo;
    private final ProyectoRepository proyectoRepo;
    private final ImagenMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public List<ImagenDTO> listarPorProyecto(Integer idProyecto) {
        List<Imagen> imagenes = imagenRepo.findByProyectoImagen_IdProyecto(idProyecto);
        return mapper.toDtoList(imagenes);
    }

    @Override
    public ImagenDTO guardar(ImagenDTO dto, Integer idProyecto) {
        Proyecto proyecto = proyectoRepo.findById(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        Imagen imagen = mapper.toEntity(dto);
        imagen.setProyectoImagen(proyecto);
        Imagen guardada = imagenRepo.save(imagen);
        return mapper.toDto(guardada);
    }

    @Override
    public void eliminar(Integer idImagen) {
        if (!imagenRepo.existsById(idImagen)) {
            throw new IllegalArgumentException("Imagen no encontrada");
        }

        imagenRepo.deleteById(idImagen);
    }
}
