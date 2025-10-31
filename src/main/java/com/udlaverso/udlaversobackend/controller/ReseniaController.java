package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.ReseniaDTO;
import com.udlaverso.udlaversobackend.entity.Usuario;
import com.udlaverso.udlaversobackend.service.ReseniaService;
import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import com.udlaverso.udlaversobackend.util.PermisoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resenias")
@RequiredArgsConstructor
public class ReseniaController {

    private final ReseniaService servicio;
    private final UsuarioRepository usuarioRepo;

    @PostMapping("/{proyectoId}")
    public ResponseEntity<ReseniaDTO> crear(
            @PathVariable Integer proyectoId,
            @RequestBody ReseniaDTO dto,
            Authentication auth
    ) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que el usuario tenga permiso de escribir reseñas (opcional)
        if (!PermisoUtils.tienePermiso(usuario, "escribir_reseña")) {
            return ResponseEntity.status(403).body(null);
        }

        ReseniaDTO creada = servicio.crearResenia(dto, proyectoId, usuario.getIdUsuario());
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<ReseniaDTO>> listarPorProyecto(@PathVariable Integer proyectoId) {
        return ResponseEntity.ok(servicio.listarReseniasPorProyecto(proyectoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReseniaDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(servicio.obtenerReseniaPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReseniaDTO> actualizar(@PathVariable Integer id, @RequestBody ReseniaDTO dto, Authentication auth) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(usuario, "escribir_reseña")) {
            return ResponseEntity.status(403).body(null);
        }

        ReseniaDTO actualizada = servicio.actualizarResenia(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id, Authentication auth) {
        var userDetails = (UserDetails) auth.getPrincipal();
        var correo = userDetails.getUsername();

        Usuario usuario = usuarioRepo.findByCorreoUsuarioConPermisos(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!PermisoUtils.tienePermiso(usuario, "escribir_reseña")) {
            return ResponseEntity.status(403).body(null);
        }

        servicio.eliminarResenia(id);
        return ResponseEntity.noContent().build();
    }
}
