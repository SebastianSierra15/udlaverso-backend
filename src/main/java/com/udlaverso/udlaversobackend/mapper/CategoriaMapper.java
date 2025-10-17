package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.CategoriaDTO;
import com.udlaverso.udlaversobackend.entity.Categoria;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaDTO toDto(Categoria categoria);
    Categoria toEntity(CategoriaDTO categoriaDTO);
    List<CategoriaDTO> toDtoList(List<Categoria> categorias);
}
