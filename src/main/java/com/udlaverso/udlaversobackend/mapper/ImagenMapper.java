package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ImagenDTO;
import com.udlaverso.udlaversobackend.entity.Imagen;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    @Mapping(source = "proyectoImagen.idProyecto", target = "proyectoId")
    ImagenDTO toDto(Imagen imagen);

    @Mapping(target = "proyectoImagen", expression = "java(mapProyecto(dto.getProyectoId()))")
    Imagen toEntity(ImagenDTO dto);

    List<ImagenDTO> toDtoList(List<Imagen> imagenes);

    List<Imagen> toEntityList(List<ImagenDTO> imagens);

    default Proyecto mapProyecto(Integer id) {
        if (id == null) return null;
        Proyecto proyecto = new Proyecto();
        proyecto.setIdProyecto(id);
        return proyecto;
    }
}
