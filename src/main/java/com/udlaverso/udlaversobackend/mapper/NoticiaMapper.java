package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.NoticiaDTO;
import com.udlaverso.udlaversobackend.entity.Noticia;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NoticiaMapper {
    NoticiaDTO toDTO(Noticia entity);

    Noticia toEntity(NoticiaDTO dto);
}
