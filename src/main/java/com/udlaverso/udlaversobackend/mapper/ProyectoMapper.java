package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProyectoMapper {

    @Mapping(source = "categoriaProyecto.idCategoria", target = "categoriaId")
    @Mapping(source = "categoriaProyecto.nombreCategoria", target = "categoriaNombre")
    ProyectoDTO toDto(Proyecto proyecto);

    @InheritInverseConfiguration
    @Mapping(target = "categoriaProyecto", ignore = true)
    Proyecto toEntity(ProyectoDTO dto);
}
