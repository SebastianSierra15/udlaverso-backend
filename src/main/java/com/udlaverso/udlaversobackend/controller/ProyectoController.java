package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import com.udlaverso.udlaversobackend.entity.Usuario;
import com.udlaverso.udlaversobackend.service.ProyectoService;
import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import com.udlaverso.udlaversobackend.util.PermisoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService servicio;
    private final UsuarioRepository usuarioRepo;

    @PostMapping
    public ResponseEntity<ProyectoDTO> crear(@RequestBody ProyectoDTO dto) {
        return ResponseEntity.ok(servicio.crear(dto));
    }

    @PostMapping(value = "/con-imagenes", consumes = {"multipart/form-data"})
    public ResponseEntity<?> crearConImagenes(
            @RequestPart("proyecto") ProyectoDTO dto,
            @RequestPart("hero") MultipartFile hero,
            @RequestPart(value = "galeria", required = false) List<MultipartFile> galeria,
            Authentication auth
    ) {
        var usuario = (UserDetails) auth.getPrincipal();
        var correo = usuario.getUsername();

        Usuario userEntity = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(userEntity, "crear_proyectos")) {
            return ResponseEntity.status(403).body(Map.of("error", "No tiene permiso para crear proyectos"));
        }

        ProyectoDTO creado = servicio.crearConImagenes(dto, hero, galeria);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(servicio.obtener(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ProyectoDTO> obtenerPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(servicio.obtenerPorNombre(nombre));
    }

    @GetMapping
    public ResponseEntity<Object> listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "visualizacionesProyecto,desc") String sort
    ) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(s[1]), s[0]));
        Page<ProyectoDTO> proyectosPage = servicio.listar(q, categoria, pageable);

        return ResponseEntity.ok(
                Map.of(
                        "content", proyectosPage.getContent(),
                        "total", proyectosPage.getTotalElements(),
                        "page", proyectosPage.getNumber(),
                        "pages", proyectosPage.getTotalPages()
                )
        );
    }

    @GetMapping("/mas-vistos")
    public ResponseEntity<List<ProyectoDTO>> listarMasVistos(
            @RequestParam(defaultValue = "10") int limite) {
        return ResponseEntity.ok(servicio.listarMasVistos(limite));
    }

    @GetMapping("/validar-nombre")
    public ResponseEntity<Map<String, Object>> validarNombre(
            @RequestParam String nombre,
            @RequestParam(required = false) Integer excluirId
    ) {
        boolean disponible = servicio.nombreDisponible(nombre, excluirId);
        return ResponseEntity.ok(Map.of("disponible", disponible));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> actualizar(
            @PathVariable Integer id,
            @Validated @RequestBody ProyectoDTO dto,
            Authentication auth
    ) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        // Buscar el usuario real en la BD con sus permisos
        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar permisos antes de continuar
        if (!PermisoUtils.tienePermiso(usuario, "editar_proyectos")) {
            return ResponseEntity.status(403)
                    .body(null);
        }

        ProyectoDTO actualizado = servicio.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping(value = "/{id}/con-imagenes", consumes = {"multipart/form-data"})
    public ResponseEntity<?> actualizarConImagenes(
            @PathVariable Integer id,
            @RequestPart("proyecto") ProyectoDTO dto,
            @RequestPart(value = "hero", required = false) MultipartFile hero,
            @RequestPart(value = "galeria", required = false) List<MultipartFile> galeria,
            @RequestPart(value = "imagenesEliminadas", required = false) String imagenesEliminadasJson,
            Authentication auth
    ) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(usuario, "editar_proyectos")) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "No tiene permiso para editar proyectos"));
        }

        ProyectoDTO actualizado = servicio.actualizarConImagenes(id, dto, hero, galeria, imagenesEliminadasJson);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}