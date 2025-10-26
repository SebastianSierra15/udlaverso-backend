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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
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

    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> actualizar(@PathVariable Integer id, @RequestBody ProyectoDTO dto) {
        return ResponseEntity.ok(servicio.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}