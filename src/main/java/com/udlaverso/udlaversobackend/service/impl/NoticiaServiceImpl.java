package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import com.udlaverso.udlaversobackend.entity.Noticia;
import com.udlaverso.udlaversobackend.mapper.NoticiaMapper;
import com.udlaverso.udlaversobackend.repository.NoticiaRepository;
import com.udlaverso.udlaversobackend.service.NoticiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticiaServiceImpl implements NoticiaService {

    private final NoticiaRepository repo;
    private final NoticiaMapper mapper;

    @Override
    public List<NoticiaDTO> listar() {
        return repo.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public NoticiaDTO obtenerPorId(Integer id) {
        return repo.findById(id).map(mapper::toDTO).orElseThrow(() -> new RuntimeException("Noticia no encontrada"));
    }

    @Override
    public NoticiaDTO obtenerPorTitulo(String titulo) {
        return repo.findByTituloNoticia(titulo)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));
    }

    @Override
    public NoticiaDTO crear(NoticiaDTO dto) {
        if (repo.existsByTituloNoticia((dto.getTituloNoticia())))
            throw new IllegalArgumentException("La noticia ya está registrada");
        Noticia entity = mapper.toEntity(dto);
        entity.setFechapublicacionNoticia(LocalDateTime.now());
        entity.setEstadoNoticia((byte) 1);
        return mapper.toDTO(repo.save(entity));
    }

    @Override
    public NoticiaDTO actualizar(Integer id, NoticiaDTO dto) {
        Noticia n = repo.findById(id).orElseThrow(() -> new RuntimeException("No existe la noticia"));
        n.setTituloNoticia(dto.getTituloNoticia());
        n.setContenidoNoticia(dto.getContenidoNoticia());
        n.setImagenNoticia(dto.getImagenNoticia());
        n.setEstadoNoticia(dto.getEstadoNoticia());
        return mapper.toDTO(repo.save(n));
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
