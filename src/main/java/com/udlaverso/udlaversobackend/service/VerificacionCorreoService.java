package com.udlaverso.udlaversobackend.service;

public interface VerificacionCorreoService {
    void enviarCodigo(String correo, String tipo);

    boolean verificarCodigo(String correo, String codigo, String tipo);
}
