package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import com.udlaverso.udlaversobackend.entity.*;
import com.udlaverso.udlaversobackend.mapper.ProyectoMapper;
import com.udlaverso.udlaversobackend.repository.*;
import com.udlaverso.udlaversobackend.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepo;
    private final CategoriaRepository categoriaRepo;
    private final ProyectoMapper mapper;

    @Override
    public ProyectoDTO crear(ProyectoDTO dto) {
        Proyecto entity = mapper.toEntity(dto);
        if (dto.getCategoriaId() != null) {
            Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            entity.setCategoria(cat);
        }
        return mapper.toDto(proyectoRepo.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDTO obtener(Integer id) {
        return proyectoRepo.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProyectoDTO> listar(String q, String categoria, Pageable pageable) {
        Page<Proyecto> page;
        if (categoria != null && !categoria.isBlank())
            page = proyectoRepo.findByCategoria_NombreIgnoreCase(categoria, pageable);
        else if (q != null && !q.isBlank())
            page = proyectoRepo.findByNombreContainingIgnoreCase(q, pageable);
        else
            page = proyectoRepo.findAll(pageable);

        return page.map(mapper::toDto);
    }

    @Override
    public ProyectoDTO actualizar(Integer id, ProyectoDTO dto) {
        Proyecto actual = proyectoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        Proyecto cambios = mapper.toEntity(dto);

        actual.setNombre(cambios.getNombre());
        actual.setDescripcionCorta(cambios.getDescripcionCorta());
        actual.setDescripcionLarga(cambios.getDescripcionLarga());
        actual.setObjetivo(cambios.getObjetivo());
        actual.setVideo(cambios.getVideo());
        actual.setAutor(cambios.getAutor());
        actual.setVisualizaciones(cambios.getVisualizaciones());
        actual.setEstado(cambios.getEstado());

        if (dto.getCategoriaId() != null) {
            Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            actual.setCategoria(cat);
        }
        return mapper.toDto(actual);
    }

    @Override
    public void eliminar(Integer id) {
        proyectoRepo.deleteById(id);
    }
}