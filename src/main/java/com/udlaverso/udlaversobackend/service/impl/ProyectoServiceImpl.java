package com.udlaverso.udlaversobackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepo;
    private final CategoriaRepository categoriaRepo;
    private final ProyectoMapper mapper;
    private final ImagenRepository imagenRepo;

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
    @Transactional
    public ProyectoDTO actualizar(Integer idProyecto, ProyectoDTO dto) {
        var existente = proyectoRepo.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        // Validación: nombre único (ignorando el actual)
        if (dto.getNombreProyecto() != null &&
                proyectoRepo.existsByNombreProyectoIgnoreCaseAndIdProyectoNot(
                        dto.getNombreProyecto().trim(), idProyecto)) {
            throw new RuntimeException("El nombre del proyecto ya existe");
        }

        // Actualiza campos permitidos
        if (dto.getNombreProyecto() != null) existente.setNombreProyecto(dto.getNombreProyecto().trim());
        if (dto.getAutorProyecto() != null) existente.setAutorProyecto(dto.getAutorProyecto());
        if (dto.getObjetivoProyecto() != null) existente.setObjetivoProyecto(dto.getObjetivoProyecto());
        if (dto.getDescripcioncortaProyecto() != null)
            existente.setDescripcioncortaProyecto(dto.getDescripcioncortaProyecto());
        if (dto.getDescripcionlargaProyecto() != null)
            existente.setDescripcionlargaProyecto(dto.getDescripcionlargaProyecto());
        if (dto.getVideoProyecto() != null) existente.setVideoProyecto(dto.getVideoProyecto());
        if (dto.getHerramientasProyecto() != null) existente.setHerramientasProyecto(dto.getHerramientasProyecto());
        if (dto.getPalabrasclaveProyecto() != null) existente.setPalabrasclaveProyecto(dto.getPalabrasclaveProyecto());
        if (dto.getCategoriaId() != null) existente.getCategoriaProyecto().setIdCategoria(dto.getCategoriaId());

        var guardado = proyectoRepo.save(existente);
        return mapper.toDto(guardado);
    }

    @Override
    @Transactional
    public ProyectoDTO actualizarConImagenes(Integer idProyecto, ProyectoDTO dto, MultipartFile hero, List<MultipartFile> galeria, String imagenesEliminadasJson) {
        Proyecto existente = proyectoRepo.findById(idProyecto)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        System.out.println("──────────────────────────────");
        System.out.println("🛰️ [DEBUG] REQUEST RECIBIDA BACKEND:");
        System.out.println("🆔 ID Proyecto: " + idProyecto);
        System.out.println("📦 Hero: " + (hero != null ? hero.getOriginalFilename() : "Ninguno"));
        System.out.println("📦 Galería: " + (galeria != null ? galeria.size() : "null"));
        System.out.println("📦 imagenesEliminadasJson (raw): " + imagenesEliminadasJson);
        System.out.println("📦 dto.imagenesEliminadas: " + dto.getImagenesEliminadas());
        System.out.println("──────────────────────────────");

        // --- Actualiza campos de texto
        if (dto.getNombreProyecto() != null) existente.setNombreProyecto(dto.getNombreProyecto());
        if (dto.getAutorProyecto() != null) existente.setAutorProyecto(dto.getAutorProyecto());
        if (dto.getDescripcioncortaProyecto() != null)
            existente.setDescripcioncortaProyecto(dto.getDescripcioncortaProyecto());
        if (dto.getDescripcionlargaProyecto() != null)
            existente.setDescripcionlargaProyecto(dto.getDescripcionlargaProyecto());
        if (dto.getObjetivoProyecto() != null) existente.setObjetivoProyecto(dto.getObjetivoProyecto());
        if (dto.getVideoProyecto() != null) existente.setVideoProyecto(dto.getVideoProyecto());
        if (dto.getHerramientasProyecto() != null) existente.setHerramientasProyecto(dto.getHerramientasProyecto());
        if (dto.getPalabrasclaveProyecto() != null) existente.setPalabrasclaveProyecto(dto.getPalabrasclaveProyecto());

        if (dto.getCategoriaId() != null) {
            Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            existente.setCategoriaProyecto(cat);
        }

        Path uploadDir = Paths.get("uploads/proyectos/" + existente.getIdProyecto());
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Error al crear directorio del proyecto", e);
        }

        System.out.println("📸 Imágenes actuales en BD antes de actualizar:");
        existente.getImagenesProyecto().forEach(img ->
                System.out.println("   -> " + img.getTipoImagen() + " | " + img.getRutaImagen()));

        // --- 🔥 Eliminar imágenes marcadas ---
        if (imagenesEliminadasJson != null && !imagenesEliminadasJson.isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                List<String> rutasAEliminar = Arrays.asList(mapper.readValue(imagenesEliminadasJson, String[].class));

                if (!rutasAEliminar.isEmpty()) {
                    List<Imagen> imagenesProyecto = imagenRepo.findByProyectoImagen_IdProyecto(existente.getIdProyecto());

                    for (String ruta : rutasAEliminar) {
                        imagenesProyecto.stream()
                                // Usa contains() para emparejar rutas absolutas (http://localhost...) con las relativas (/uploads/...)
                                .filter(img -> ruta.contains(img.getRutaImagen()))
                                .findFirst()
                                .ifPresent(img -> {
                                    try {
                                        Path archivo = Paths.get("." + img.getRutaImagen());
                                        if (Files.deleteIfExists(archivo)) {
                                            System.out.println("🧩 Imagen eliminada del disco: " + archivo);
                                        } else {
                                            System.out.println("⚠️ No se encontró en disco: " + archivo);
                                        }
                                    } catch (IOException e) {
                                        System.err.println("⚠️ Error al borrar archivo: " + img.getRutaImagen());
                                    }

                                    // Elimina de la base de datos
                                    imagenRepo.delete(img);

                                    // Elimina de la relación con el proyecto
                                    existente.getImagenesProyecto().remove(img);

                                    System.out.println("🗑️ Imagen eliminada de BD: " + img.getRutaImagen());
                                });
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar JSON de imágenes eliminadas", e);
            }
        }

        // --- Reemplazar hero solo si viene uno nuevo
        if (hero != null) {
            // Eliminar el anterior
            List<Imagen> aEliminarHero = existente.getImagenesProyecto().stream()
                    .filter(img -> "hero".equals(img.getTipoImagen()))
                    .toList();
            existente.getImagenesProyecto().removeAll(aEliminarHero);

            for (Imagen img : aEliminarHero) {
                try {
                    Files.deleteIfExists(Paths.get("." + img.getRutaImagen()));
                } catch (IOException e) {
                    System.err.println("⚠️ No se pudo borrar hero antiguo: " + img.getRutaImagen());
                }
            }

            try {
                String heroNombre = "hero_" + System.currentTimeMillis() + "_" + hero.getOriginalFilename();
                Path destino = uploadDir.resolve(heroNombre);
                Files.copy(hero.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

                Imagen heroImg = new Imagen();
                heroImg.setRutaImagen("/uploads/proyectos/" + existente.getIdProyecto() + "/" + heroNombre);
                heroImg.setTipoImagen("hero");
                heroImg.setProyectoImagen(existente);

                existente.getImagenesProyecto().add(heroImg);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen principal", e);
            }
        }

        // --- Agregar nuevas imágenes de galería (solo si se enviaron)
        if (galeria != null && !galeria.isEmpty()) {
            int i = 1;
            for (MultipartFile imgFile : galeria) {
                try {
                    String fileName = "galeria_" + i + "_" + imgFile.getOriginalFilename();
                    Path destino = uploadDir.resolve(fileName);
                    Files.copy(imgFile.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

                    Imagen img = new Imagen();
                    img.setRutaImagen("/uploads/proyectos/" + existente.getIdProyecto() + "/" + fileName);
                    img.setTipoImagen("galeria");
                    img.setProyectoImagen(existente);

                    existente.getImagenesProyecto().add(img);
                    i++;
                } catch (IOException e) {
                    throw new RuntimeException("Error al guardar una imagen de galería", e);
                }
            }
        }

        // --- Solo guarda si hubo cambios
        Proyecto guardado = proyectoRepo.save(existente);

        System.out.println("💾 Proyecto guardado con " + guardado.getImagenesProyecto().size() + " imágenes:");
        guardado.getImagenesProyecto().forEach(img ->
                System.out.println("   ✅ " + img.getTipoImagen() + " | " + img.getRutaImagen()));
        System.out.println("──────────────────────────────");

        return mapper.toDto(guardado);
    }

    @Override
    public boolean nombreDisponible(String nombre, Integer excluirId) {
        if (nombre == null || nombre.isBlank()) return false;
        if (excluirId == null) {
            return !proyectoRepo.existsByNombreProyectoIgnoreCase(nombre.trim());
        }
        return !proyectoRepo.existsByNombreProyectoIgnoreCaseAndIdProyectoNot(nombre.trim(), excluirId);
    }

    @Override
    public void eliminar(Integer id) {
        proyectoRepo.deleteById(id);
    }
}