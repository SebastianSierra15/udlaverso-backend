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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            entity.setCategoriaProyecto(cat);
        }

        entity.setFechacreacionProyecto(LocalDate.now());
        entity.setVisualizacionesProyecto(0);
        entity.setEstadoProyecto((byte) 1);

        Proyecto guardado = proyectoRepo.save(entity);
        return mapper.toDto(guardado);
    }

    @Override
    public ProyectoDTO crearConImagenes(ProyectoDTO dto, MultipartFile hero, List<MultipartFile> galeria) {
        Proyecto entity = mapper.toEntity(dto);

        if (dto.getCategoriaId() != null) {
            Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            entity.setCategoriaProyecto(cat);
        }

        entity.setFechacreacionProyecto(LocalDate.now());
        entity.setVisualizacionesProyecto(0);
        entity.setEstadoProyecto((byte) 1);
        entity.setHerramientasProyecto(dto.getHerramientasProyecto());
        entity.setPalabrasclaveProyecto(dto.getPalabrasclaveProyecto());
        entity.setAutorProyecto(dto.getAutorProyecto());

        Proyecto guardado = proyectoRepo.save(entity);
        if (guardado.getImagenesProyecto() == null) {
            guardado.setImagenesProyecto(new ArrayList<>());
        }

        // Crear carpeta si no existe
        Path uploadDir = Paths.get("uploads/proyectos/" + guardado.getIdProyecto());
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear directorio de proyecto", e);
        }

        try {
            // Guardar imagen principal (sin convertir)
            String heroNombre = "hero_" + System.currentTimeMillis() + "_" + hero.getOriginalFilename();
            Path destinoHero = uploadDir.resolve(heroNombre);
            Files.copy(hero.getInputStream(), destinoHero, StandardCopyOption.REPLACE_EXISTING);

            Imagen heroImg = new Imagen();
            heroImg.setRutaImagen("/uploads/proyectos/" + guardado.getIdProyecto() + "/" + heroNombre);
            heroImg.setTipoImagen("hero");
            heroImg.setProyectoImagen(guardado);
            guardado.getImagenesProyecto().add(heroImg);

            // Guardar galería (sin convertir)
            if (galeria != null && !galeria.isEmpty()) {
                int i = 1;
                for (MultipartFile file : galeria) {
                    String fileName = "galeria_" + i + "_" + file.getOriginalFilename();
                    Path destino = uploadDir.resolve(fileName);
                    Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

                    Imagen img = new Imagen();
                    img.setRutaImagen("/uploads/proyectos/" + guardado.getIdProyecto() + "/" + fileName);
                    img.setTipoImagen("galeria");
                    img.setProyectoImagen(guardado);
                    guardado.getImagenesProyecto().add(img);
                    i++;
                }
            }

            proyectoRepo.save(guardado);
            return mapper.toDto(guardado);

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar imágenes del proyecto", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDTO obtener(Integer id) {
        return proyectoRepo.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoDTO obtenerPorNombre(String slug) {
        Proyecto proyecto = proyectoRepo
                .findBySlugConResenias(slug)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        ProyectoDTO dto = mapper.toDto(proyecto);

        // Calcular el promedio de valoracion
        if (proyecto.getReseniasProyecto() != null && !proyecto.getReseniasProyecto().isEmpty()) {
            double promedio = proyecto.getReseniasProyecto().stream()
                    .mapToDouble(r -> r.getValoracionResenia())
                    .average()
                    .orElse(0.0);
            dto.setValoracionPromedio(promedio);
        } else {
            dto.setValoracionPromedio(0.0);
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProyectoDTO> listar(String q, String categoria, Pageable pageable) {
        Page<Proyecto> page;
        if (categoria != null && !categoria.isBlank())
            page = proyectoRepo.findByCategoriaProyecto_NombreCategoriaIgnoreCase(categoria, pageable);
        else if (q != null && !q.isBlank())
            page = proyectoRepo.findByNombreProyectoContainingIgnoreCase(q, pageable);
        else
            page = proyectoRepo.findAll(pageable);

        return page.map(proyecto -> {
            ProyectoDTO dto = mapper.toDto(proyecto);
            // Calcular el promedio de valoracion
            if (proyecto.getReseniasProyecto() != null && !proyecto.getReseniasProyecto().isEmpty()) {
                double promedio = proyecto.getReseniasProyecto().stream()
                        .mapToDouble(r -> r.getValoracionResenia())
                        .average()
                        .orElse(0.0);
                dto.setValoracionPromedio(promedio);
            } else {
                dto.setValoracionPromedio(0.0);
            }
            return dto;
        });
    }

    @Override
    public List<ProyectoDTO> listarMasVistos(int limite) {
        var proyectos = proyectoRepo.findTopProyectosMasVistos(PageRequest.of(0, limite));
        return proyectos.stream().map(mapper::toDto).toList();
    }

    @Override
    public ProyectoDTO actualizar(Integer id, ProyectoDTO dto) {
        Proyecto actual = proyectoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        Proyecto cambios = mapper.toEntity(dto);

        actual.setNombreProyecto(cambios.getNombreProyecto());
        actual.setDescripcioncortaProyecto(cambios.getDescripcioncortaProyecto());
        actual.setDescripcionlargaProyecto(cambios.getDescripcionlargaProyecto());
        actual.setObjetivoProyecto(cambios.getObjetivoProyecto());
        actual.setVideoProyecto(cambios.getVideoProyecto());
        actual.setAutorProyecto(cambios.getAutorProyecto());
        actual.setVisualizacionesProyecto(cambios.getVisualizacionesProyecto());
        actual.setEstadoProyecto(cambios.getEstadoProyecto());

        if (dto.getCategoriaId() != null) {
            Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            actual.setCategoriaProyecto(cat);
        }
        return mapper.toDto(actual);
    }

    @Override
    public void eliminar(Integer id) {
        proyectoRepo.deleteById(id);
    }
}