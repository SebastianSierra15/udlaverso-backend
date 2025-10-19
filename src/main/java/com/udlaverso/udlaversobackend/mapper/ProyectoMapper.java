package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ReseniaMapper.class})
public interface ProyectoMapper {

    @Mapping(source = "categoriaProyecto.idCategoria", target = "categoriaId")
    @Mapping(source = "categoriaProyecto.nombreCategoria", target = "categoriaNombre")
    @Mapping(source = "reseniasProyecto", target = "resenias")
    ProyectoDTO toDto(Proyecto proyecto);

    @InheritInverseConfiguration
    @Mapping(target = "categoriaProyecto", ignore = true)
    Proyecto toEntity(ProyectoDTO dto);
}
