package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.ProyectoDTO;
import com.udlaverso.udlaversobackend.entity.Categoria;
import com.udlaverso.udlaversobackend.entity.Proyecto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ReseniaMapper.class, ImagenMapper.class})
public interface ProyectoMapper {

    // ====== De Entidad a DTO ======
    @Mapping(source = "categoriaProyecto.idCategoria", target = "categoriaId")
    @Mapping(source = "categoriaProyecto.nombreCategoria", target = "categoriaNombre")
    @Mapping(source = "reseniasProyecto", target = "resenias")
    @Mapping(target = "imagenesEliminadas", ignore = true)
    @Mapping(target = "valoracionPromedio", ignore = true)
    ProyectoDTO toDto(Proyecto proyecto);

    // ====== De DTO a Entidad ======
    @InheritInverseConfiguration(name = "toDto")
    @Mapping(target = "categoriaProyecto", expression = "java(mapCategoria(dto.getCategoriaId()))")
    @Mapping(target = "imagenesProyecto", ignore = true)
    @Mapping(target = "reseniasProyecto", ignore = true)
    Proyecto toEntity(ProyectoDTO dto);

    // Metodo auxiliar para construir una categoría mínima con el ID
    default Categoria mapCategoria(Integer id) {
        if (id == null) return null;
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(id);
        return categoria;
    }
}
