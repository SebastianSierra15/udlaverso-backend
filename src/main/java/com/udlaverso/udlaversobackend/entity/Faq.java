package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "faq")
@Getter
@Setter
@NoArgsConstructor
public class Faq {
    @Id
    @Column(name = "id_faq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFaq;

    @Column(name = "pregunta_faq", length = 255)
    private String preguntaFaq;

    @Column(name = "respuesta_faq", length = 1000)
    private String respuestaFaq;

    @Column(name = "estado_faq")
    private Byte estadoFaq;
}
