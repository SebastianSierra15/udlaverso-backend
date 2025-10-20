package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.dto.FaqDTO;
import com.udlaverso.udlaversobackend.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService servicio;

    @GetMapping
    public List<FaqDTO> listar() {
        return servicio.listar();
    }
}
