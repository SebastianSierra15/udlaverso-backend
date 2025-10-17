package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.ImagenDTO;
import com.udlaverso.udlaversobackend.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagenes")
@RequiredArgsConstructor
public class ImagenController {
    private final ImagenService servicio;

    @GetMapping("/proyecto/{idProyecto}")
    public ResponseEntity<List<ImagenDTO>> listarPorProyecto(@PathVariable Integer idProyecto) {
        return ResponseEntity.ok(servicio.listarPorProyecto(idProyecto));
    }

    @PostMapping("/proyecto/{idProyecto}")
    public ResponseEntity<ImagenDTO> guardar(@PathVariable Integer idProyecto, @RequestBody ImagenDTO dto) {
        return ResponseEntity.ok(servicio.guardar(dto, idProyecto));
    }

    @DeleteMapping("/{idImagen}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idImagen) {
        servicio.eliminar(idImagen);
        return ResponseEntity.noContent().build();
    }
}
