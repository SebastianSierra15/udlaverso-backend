package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import com.udlaverso.udlaversobackend.entity.Noticia;
import com.udlaverso.udlaversobackend.mapper.NoticiaMapper;
import com.udlaverso.udlaversobackend.repository.NoticiaRepository;
import com.udlaverso.udlaversobackend.service.NoticiaService;
import com.udlaverso.udlaversobackend.util.FileNameUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticiaServiceImpl implements NoticiaService {

    private final NoticiaRepository repo;
    private final NoticiaMapper mapper;

    @Override
    public Page<NoticiaDTO> listar(String q, Pageable pageable) {
        Page<Noticia> page;

        if (q != null && !q.isBlank())
            page = repo.findByTituloNoticiaContainingIgnoreCase(q, pageable);
        else
            page = repo.findAll(pageable);

        return page.map(mapper::toDTO);
    }

    @Override
    public List<NoticiaDTO> listarRecientes() {
        // Obtiene las últimas 6 noticias ordenadas por fecha DESC
        Pageable topSix = PageRequest.of(0, 6, Sort.by("fechapublicacionNoticia").descending());
        return repo.findAll(topSix)
                .getContent()
                .stream()
                .map(mapper::toDTO)
                .toList();
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
    public NoticiaDTO crearConImagen(NoticiaDTO dto, MultipartFile imagen) {
        // 1️⃣ Validar título duplicado
        if (repo.existsByTituloNoticia(dto.getTituloNoticia())) {
            throw new IllegalArgumentException("La noticia ya está registrada");
        }

        // 2️⃣ Validar que venga la imagen
        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar una imagen para la noticia");
        }

        // 3️⃣ Convertir y configurar entidad
        Noticia entity = mapper.toEntity(dto);
        entity.setFechapublicacionNoticia(LocalDateTime.now());
        entity.setEstadoNoticia((byte) 1);

        // ⚠️ Evitar error SQL: inicializamos con cadena vacía para que nunca sea null
        entity.setImagenNoticia("");

        // 4️⃣ Guardar noticia base (necesitamos su ID)
        Noticia guardada = repo.save(entity);

        // 5️⃣ Crear carpeta para la imagen
        Path uploadDir = Paths.get("uploads/noticias/" + guardada.getIdNoticia());
        try {
            Files.createDirectories(uploadDir);

            // 6️⃣ Guardar el archivo físicamente
            String nombreArchivo = "imagen_" + System.currentTimeMillis() + "_" + FileNameUtils.sanitize(imagen.getOriginalFilename());
            Path destino = uploadDir.resolve(nombreArchivo);
            Files.copy(imagen.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // 7️⃣ Actualizar la ruta en la entidad
            guardada.setImagenNoticia("/uploads/noticias/" + guardada.getIdNoticia() + "/" + nombreArchivo);
            repo.save(guardada);

            System.out.println("✅ Imagen de noticia guardada en: " + guardada.getImagenNoticia());

            // 8️⃣ Retornar DTO final
            return mapper.toDTO(guardada);

        } catch (IOException e) {
            // 9️⃣ En caso de error, limpiar registro inicial si es necesario
            repo.deleteById(guardada.getIdNoticia());
            throw new RuntimeException("Error al guardar la imagen de la noticia", e);
        }
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
    public NoticiaDTO actualizarConImagen(Integer id, NoticiaDTO dto, MultipartFile imagen) {
        Noticia existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        // Actualizar campos editables
        if (dto.getTituloNoticia() != null)
            existente.setTituloNoticia(dto.getTituloNoticia());
        if (dto.getContenidoNoticia() != null)
            existente.setContenidoNoticia(dto.getContenidoNoticia());

        // Manejar nueva imagen
        if (imagen != null && !imagen.isEmpty()) {
            Path uploadDir = Paths.get("uploads/noticias/" + existente.getIdNoticia());
            try {
                Files.createDirectories(uploadDir);

                // Eliminar la anterior si existe
                if (existente.getImagenNoticia() != null) {
                    Path anterior = Paths.get("." + existente.getImagenNoticia());
                    Files.deleteIfExists(anterior);
                }

                // Guardar nueva
                String nombreArchivo = "imagen_" + System.currentTimeMillis() + "_" + FileNameUtils.sanitize(imagen.getOriginalFilename());
                Path destino = uploadDir.resolve(nombreArchivo);
                Files.copy(imagen.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

                existente.setImagenNoticia("/uploads/noticias/" + existente.getIdNoticia() + "/" + nombreArchivo);

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la nueva imagen", e);
            }
        }

        Noticia guardada = repo.save(existente);
        return mapper.toDTO(guardada);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public void eliminarNoticia(Integer id) {
        Noticia noticia = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        // Eliminar imagen si existe
        if (noticia.getImagenNoticia() != null) {
            try {
                Path imagenPath = Paths.get("uploads").resolve(
                        noticia.getImagenNoticia().replaceFirst("^/+", "")
                );
                Files.deleteIfExists(imagenPath);
            } catch (IOException e) {
                System.err.println("⚠️ Error al eliminar la imagen: " + e.getMessage());
            }
            noticia.setImagenNoticia(null);
        }

        // Marcar como eliminada
        noticia.setEstadoNoticia((byte) 3);

        repo.save(noticia);
    }
}
