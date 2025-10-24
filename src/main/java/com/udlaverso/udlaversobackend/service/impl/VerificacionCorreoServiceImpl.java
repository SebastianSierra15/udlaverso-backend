package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.entity.VerificacionCorreo;
import com.udlaverso.udlaversobackend.repository.VerificacionCorreoRepository;
import com.udlaverso.udlaversobackend.service.VerificacionCorreoService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificacionCorreoServiceImpl implements VerificacionCorreoService {

    private final VerificacionCorreoRepository repo;
    private final JavaMailSender mailSender;

    @Override
    public void enviarCodigo(String correo, String tipo) {
        String codigo = String.format("%06d", new Random().nextInt(999999));

        VerificacionCorreo ver = new VerificacionCorreo();
        ver.setCorreoVerificacionCorreo(correo);
        ver.setCodigoVerificacionCorreo(codigo);
        ver.setTipoVerificacionCorreo(tipo);
        ver.setExpiracionVerificacionCorreo(LocalDateTime.now().plusMinutes(10));
        ver.setVerificadoVerificacionCorreo(false);
        ver.setUsadoVerificacionCorreo(false);
        repo.save(ver);

        String asunto = tipo.equalsIgnoreCase("recuperacion")
                ? "Recuperación de contraseña - UdlaVerso"
                : "Código de verificación - UdlaVerso";

        String mensaje = tipo.equalsIgnoreCase("recuperacion")
                ? "Tu código para restablecer la contraseña es: " + codigo
                : "Tu código de verificación es: " + codigo;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(correo);
        email.setSubject(asunto);
        email.setText(mensaje + "\n\nEste código expira en 10 minutos.");
        mailSender.send(email);
    }

    @Override
    public boolean verificarCodigo(String correo, String codigo, String tipo) {
        var verOpt = repo.findTopByCorreoVerificacionCorreoAndTipoVerificacionCorreoOrderByExpiracionVerificacionCorreoDesc(correo, tipo);
        if (verOpt.isEmpty()) return false;

        var ver = verOpt.get();
        if (ver.isUsadoVerificacionCorreo() || ver.getExpiracionVerificacionCorreo().isBefore(LocalDateTime.now()))
            return false;

        if (!ver.getCodigoVerificacionCorreo().equals(codigo))
            return false;

        ver.setVerificadoVerificacionCorreo(true);
        ver.setUsadoVerificacionCorreo(true);
        repo.save(ver);
        return true;
    }
}
