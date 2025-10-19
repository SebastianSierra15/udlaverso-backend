package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ImagenDTO;
import com.udlaverso.udlaversobackend.entity.Imagen;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    ImagenDTO toDto(Imagen imagen);

    Imagen toEntity(ImagenDTO dto);

    List<ImagenDTO> toDtoList(List<Imagen> imagenes);

    List<Imagen> toEntityList(List<ImagenDTO> imagens);
}
