package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.CategoriaDTO;
import com.udlaverso.udlaversobackend.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService servicio;

    @GetMapping
    public List<CategoriaDTO> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public CategoriaDTO obtenerPorId(@PathVariable Integer id) {
        return servicio.obtenerPorId(id);
    }

    @PostMapping
    public CategoriaDTO crear(@RequestBody CategoriaDTO dto) {
        return servicio.crear(dto);
    }

    @PutMapping("/{id}")
    public CategoriaDTO actualizar(@PathVariable Integer id, @RequestBody CategoriaDTO dto) {
        return servicio.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
