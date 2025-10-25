package com.udlaverso.udlaversobackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verificacioncorreo")
@Getter
@Setter
@NoArgsConstructor
public class VerificacionCorreo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_verificacioncorreo")
    private Integer idVerificacionCorreo;

    @Column(name = "correo_verificacioncorreo")
    private String correoVerificacionCorreo;

    @Column(name = "codigo_verificacioncorreo")
    private String codigoVerificacionCorreo;

    @Column(name = "intentos_verificacioncorreo")
    private int intentosVerificacionCorreo;

    @Column(name = "bloqueohasta_verificacioncorreo")
    private LocalDateTime bloqueoHastaVerificacionCorreo;

    @Column(name = "fechacreacion_verificacioncorreo")
    private LocalDateTime fechacreacionVerificacionCorreo;

    @Column(name = "expiracion_verificacioncorreo")
    private LocalDateTime expiracionVerificacionCorreo;

    @Column(name = "verificado_verificacioncorreo")
    private boolean verificadoVerificacionCorreo;

    @Column(name = "tipo_verificacioncorreo")
    private String tipoVerificacionCorreo; // valores: 'registro' o 'recuperacion'

    @Column(name = "usado_verificacioncorreo")
    private boolean usadoVerificacionCorreo;
}
