package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ReseniaDTO;
import com.udlaverso.udlaversobackend.entity.Resenia;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReseniaMapper {
    @Mapping(source = "usuarioResenia.nombresUsuario", target = "usuarioNombres")
    @Mapping(source = "usuarioResenia.apellidosUsuario", target = "usuarioApellidos")
    @Mapping(source = "valoracionResenia", target = "valoracionResenia")
    @Mapping(source = "comentarioResenia", target = "comentarioResenia")
    @Mapping(source = "fechaResenia", target = "fechaResenia")
    ReseniaDTO toDto(Resenia resenia);
}
