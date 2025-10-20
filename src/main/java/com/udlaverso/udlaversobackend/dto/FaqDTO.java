package com.udlaverso.udlaversobackend.dto;

import lombok.Data;

@Data
public class FaqDTO {
    private Integer idFaq;
    private String preguntaFaq;
    private String respuestaFaq;
    private Byte estadoFaq;
}
