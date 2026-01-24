package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.AnaliticaDTO;
import com.udlaverso.udlaversobackend.service.AnaliticaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analiticas")
@RequiredArgsConstructor
public class AnaliticaController {

    private final AnaliticaService analiticaService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarAnalitica(@RequestBody AnaliticaDTO dto, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        analiticaService.registrarAnalitica(dto, ip, userAgent);
        return ResponseEntity.ok("Analítica registrada correctamente");
    }
}
