package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.dto.FaqDTO;
import com.udlaverso.udlaversobackend.mapper.FaqMapper;
import com.udlaverso.udlaversobackend.repository.FaqRepository;
import com.udlaverso.udlaversobackend.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository repo;
    private final FaqMapper mapper;

    @Override
    public List<FaqDTO> listar() {
        return repo.findAll()
                .stream()
                .filter(f -> f.getEstadoFaq() == 1)
                .map(mapper::toDTO)
                .toList();
    }
}
