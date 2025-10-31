package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.ReseniaDTO;
import com.udlaverso.udlaversobackend.entity.Resenia;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import com.udlaverso.udlaversobackend.entity.Usuario;
import com.udlaverso.udlaversobackend.mapper.ReseniaMapper;
import com.udlaverso.udlaversobackend.repository.ReseniaRepository;
import com.udlaverso.udlaversobackend.repository.ProyectoRepository;
import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import com.udlaverso.udlaversobackend.service.ReseniaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReseniaServiceImpl implements ReseniaService {

    private final ReseniaRepository reseniaRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReseniaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ReseniaDTO obtenerReseniaPorId(Integer idResenia) {
        Resenia resenia = reseniaRepository.findById(idResenia)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
        return mapper.toDto(resenia);
    }

    @Override
    @Transactional
    public ReseniaDTO crearResenia(ReseniaDTO dto, Integer proyectoId, Integer usuarioId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Resenia resenia = new Resenia();
        resenia.setComentarioResenia(dto.getComentarioResenia());
        resenia.setValoracionResenia(dto.getValoracionResenia());
        resenia.setProyectoResenia(proyecto);
        resenia.setUsuarioResenia(usuario);
        resenia.setEstadoResenia((byte) 1);

        Resenia guardada = reseniaRepository.save(resenia);
        return mapper.toDto(guardada);
    }

    @Override
    @Transactional
    public ReseniaDTO actualizarResenia(Integer idResenia, ReseniaDTO dto) {
        Resenia resenia = reseniaRepository.findById(idResenia)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));

        resenia.setComentarioResenia(dto.getComentarioResenia());
        resenia.setValoracionResenia(dto.getValoracionResenia());

        Resenia actualizada = reseniaRepository.save(resenia);
        return mapper.toDto(actualizada);
    }

    @Override
    @Transactional
    public void eliminarResenia(Integer idResenia) {
        if (!reseniaRepository.existsById(idResenia)) {
            throw new RuntimeException("Reseña no encontrada");
        }
        reseniaRepository.deleteById(idResenia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReseniaDTO> listarReseniasPorProyecto(Integer proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        return proyecto.getReseniasProyecto().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
