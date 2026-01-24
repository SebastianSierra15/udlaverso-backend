package com.udlaverso.udlaversobackend.service;

import com.udlaverso.udlaversobackend.dto.AnaliticaDTO;

public interface AnaliticaService {
    void registrarAnalitica(AnaliticaDTO dto, String ip, String userAgent);
}
