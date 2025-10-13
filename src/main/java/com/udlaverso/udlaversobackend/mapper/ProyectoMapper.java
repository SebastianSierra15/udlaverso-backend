package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProyectoMapper {

    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(target = "imagenes", expression = "java(mapImagenes(proyecto))")
    ProyectoDTO toDto(Proyecto proyecto);

    @InheritInverseConfiguration
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    Proyecto toEntity(ProyectoDTO dto);

    // ✅ Método auxiliar usado en la expresión
    default List<String> mapImagenes(Proyecto proyecto) {
        if (proyecto == null || proyecto.getImagenes() == null)
            return Collections.emptyList();
        return proyecto.getImagenes().stream()
                .map(img -> img.getRuta())
                .collect(Collectors.toList());
    }
}
