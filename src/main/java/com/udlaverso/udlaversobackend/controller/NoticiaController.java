package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import com.udlaverso.udlaversobackend.service.NoticiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService servicio;

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

    @PutMapping("/{id}")
    public NoticiaDTO actualizar(@PathVariable Integer id, @RequestBody NoticiaDTO dto) {
        return servicio.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
    }
}
