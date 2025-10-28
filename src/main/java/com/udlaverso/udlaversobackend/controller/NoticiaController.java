package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import com.udlaverso.udlaversobackend.entity.Usuario;
import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import com.udlaverso.udlaversobackend.service.NoticiaService;
import com.udlaverso.udlaversobackend.util.PermisoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService servicio;
    private final UsuarioRepository usuarioRepo;

    @GetMapping
    public ResponseEntity<Object> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "desc") String orden
    ) {
        Sort sort = orden.equalsIgnoreCase("asc")
                ? Sort.by("fechapublicacionNoticia").ascending()
                : Sort.by("fechapublicacionNoticia").descending();

        Page<NoticiaDTO> noticiasPage = servicio.listar(q, PageRequest.of(page, size, sort));

        return ResponseEntity.ok(
                Map.of(
                        "content", noticiasPage.getContent(),
                        "total", noticiasPage.getTotalElements(),
                        "page", noticiasPage.getNumber(),
                        "pages", noticiasPage.getTotalPages()
                )
        );
    }

    @GetMapping("/recientes")
    public ResponseEntity<Object> listarRecientes() {
        List<NoticiaDTO> recientes = servicio.listarRecientes();
        return ResponseEntity.ok(recientes);
    }

    @GetMapping("/{id}")
    public NoticiaDTO obtenerPorId(@PathVariable Integer id) {
        return servicio.obtenerPorId(id);
    }

    @GetMapping("/titulo/{titulo}")
    public NoticiaDTO obtenerPorTitulo(@PathVariable String titulo) {
        return servicio.obtenerPorTitulo(titulo);
    }

    @PostMapping
    public NoticiaDTO crear(@RequestBody NoticiaDTO dto) {
        return servicio.crear(dto);
    }

    @PostMapping(value = "/con-imagen", consumes = {"multipart/form-data"})
    public ResponseEntity<?> crearConImagen(
            @RequestPart("noticia") NoticiaDTO dto,
            @RequestPart("imagen") MultipartFile imagen,
            Authentication auth
    ) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(usuario, "crear_noticias")) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "No tiene permiso para crear noticias"
            ));
        }

        System.out.println("📰 Creando noticia con imagen por: " + correo);

        NoticiaDTO creada = servicio.crearConImagen(dto, imagen);
        return ResponseEntity.ok(creada);
    }

    @PutMapping("/{id}")
    public NoticiaDTO actualizar(@PathVariable Integer id, @RequestBody NoticiaDTO dto) {
        return servicio.actualizar(id, dto);
    }

    @PutMapping(value = "/{id}/con-imagen", consumes = {"multipart/form-data"})
    public ResponseEntity<?> actualizarConImagen(
            @PathVariable Integer id,
            @RequestPart("noticia") NoticiaDTO dto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            Authentication auth
    ) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(usuario, "editar_noticias")) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "No tiene permiso para editar noticias"
            ));
        }

        NoticiaDTO actualizada = servicio.actualizarConImagen(id, dto, imagen);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNoticia(@PathVariable Integer id, Authentication auth) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(usuario, "crear_noticias")) {
            return ResponseEntity.status(403).body(Map.of("error", "No tiene permiso para eliminar noticias"));
        }

        servicio.eliminarNoticia(id);
        return ResponseEntity.ok(Map.of("mensaje", "Noticia eliminada correctamente"));
    }

}
