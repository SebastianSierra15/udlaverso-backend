package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.CategoriaDTO;
import com.udlaverso.udlaversobackend.entity.Categoria;
import com.udlaverso.udlaversobackend.mapper.CategoriaMapper;
import com.udlaverso.udlaversobackend.repository.CategoriaRepository;
import com.udlaverso.udlaversobackend.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repo;
    private final CategoriaMapper mapper;

    @Override
    public List<CategoriaDTO> listar() {
        return mapper.toDtoList(repo.findAll());
    }

    @Override
    public CategoriaDTO obtenerPorId(Integer id) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return mapper.toDto(categoria);
    }

    @Override
    public CategoriaDTO crear(CategoriaDTO dto) {
        Categoria categoria = mapper.toEntity(dto);
        Categoria guardada = repo.save(categoria);
        return mapper.toDto(guardada);
    }

    @Override
    public CategoriaDTO actualizar(Integer id, CategoriaDTO dto) {
        Categoria categoria = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setNombreCategoria(dto.getNombreCategoria());
        return mapper.toDto(repo.save(categoria));
    }

    @Override
    public void eliminar(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        repo.deleteById(id);
    }
}
