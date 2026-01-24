package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.AnaliticaDTO;
import com.udlaverso.udlaversobackend.entity.Analitica;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import com.udlaverso.udlaversobackend.entity.TipoAnalitica;
import com.udlaverso.udlaversobackend.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnaliticaMapper {

    public Analitica toEntity(AnaliticaDTO dto, Usuario usuario, Proyecto proyecto, TipoAnalitica tipo, String ip, String userAgent) {
        Analitica analitica = new Analitica();
        analitica.setDescripcionAnalitica(dto.getDescripcionAnalitica());
        analitica.setFechaeventoAnalitica(LocalDateTime.now());
        analitica.setIpusuarioAnalitica(ip);
        analitica.setUseragentAnalitica(userAgent);
        analitica.setUsuarioAnalitica(usuario);
        analitica.setProyectoAnalitica(proyecto);
        analitica.setTipoAnalitica(tipo);
        return analitica;
    }
}
