package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import com.udlaverso.udlaversobackend.service.NoticiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService servicio;

    @GetMapping
    public List<NoticiaDTO> listar() {
        return servicio.listar();
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

    @PutMapping("/{id}")
    public NoticiaDTO actualizar(@PathVariable Integer id, @RequestBody NoticiaDTO dto) {
        return servicio.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
