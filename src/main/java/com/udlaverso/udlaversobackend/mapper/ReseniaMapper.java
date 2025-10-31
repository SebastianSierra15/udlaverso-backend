package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ReseniaDTO;
import com.udlaverso.udlaversobackend.entity.Resenia;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReseniaMapper {
    @Mapping(source = "usuarioResenia.idUsuario", target = "usuarioId")
    @Mapping(source = "usuarioResenia.nombresUsuario", target = "usuarioNombres")
    @Mapping(source = "usuarioResenia.apellidosUsuario", target = "usuarioApellidos")
    ReseniaDTO toDto(Resenia resenia);
}
