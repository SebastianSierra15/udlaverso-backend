package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.AnaliticaDTO;
import com.udlaverso.udlaversobackend.entity.*;
import com.udlaverso.udlaversobackend.mapper.AnaliticaMapper;
import com.udlaverso.udlaversobackend.repository.*;
import com.udlaverso.udlaversobackend.service.AnaliticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnaliticaServiceImpl implements AnaliticaService {

    private final AnaliticaRepository analiticaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoRepository proyectoRepository;
    private final TipoAnaliticaRepository tipoAnaliticaRepository;
    private final AnaliticaMapper analiticaMapper;

    @Override
    public void registrarAnalitica(AnaliticaDTO dto, String ip, String userAgent) {
        Usuario usuario = null;
        Proyecto proyecto = null;
        TipoAnalitica tipo = null;

        if (dto.getIdUsuario() != null)
            usuario = usuarioRepository.findById(dto.getIdUsuario()).orElse(null);
        if (dto.getIdProyecto() != null)
            proyecto = proyectoRepository.findById(dto.getIdProyecto()).orElse(null);
        if (dto.getIdTipoAnalitica() != null)
            tipo = tipoAnaliticaRepository.findById(dto.getIdTipoAnalitica()).orElse(null);

        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de analítica no válido");
        }

        // === Validar duplicados de vistas (una por día / usuario / proyecto) ===
        if (tipo.getNombreTipoAnalitica().equalsIgnoreCase("vista")
                && usuario != null
                && proyecto != null) {

            boolean yaRegistrada = analiticaRepository.existsByUsuarioAnaliticaAndProyectoAnaliticaAndTipoAnaliticaAndFechaeventoAnaliticaAfter(
                    usuario,
                    proyecto,
                    tipo,
                    LocalDateTime.now().minusDays(1)
            );

            if (yaRegistrada) {
                System.out.println("⚠️ Vista ya registrada recientemente para este usuario y proyecto. No se duplica.");
                return;
            }
        }

        // === Guardar la nueva analítica ===
        Analitica analitica = analiticaMapper.toEntity(dto, usuario, proyecto, tipo, ip, userAgent);
        analiticaRepository.save(analitica);

        // === Sumar visualización si aplica ===
        if (tipo.getNombreTipoAnalitica().equalsIgnoreCase("vista") && proyecto != null) {
            Integer vistas = proyecto.getVisualizacionesProyecto();
            if (vistas == null) vistas = 0;
            proyecto.setVisualizacionesProyecto(vistas + 1);
            proyectoRepository.save(proyecto);
            System.out.println("📈 Visualización sumada al proyecto ID: " + proyecto.getIdProyecto());
        }
    }
}
