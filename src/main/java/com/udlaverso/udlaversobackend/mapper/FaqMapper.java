package com.udlaverso.udlaversobackend.mapper;

import com.udlaverso.udlaversobackend.dto.FaqDTO;
import com.udlaverso.udlaversobackend.entity.Faq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FaqMapper {
    FaqDTO toDTO(Faq entity);

    Faq toEntity(FaqDTO dto);
}
