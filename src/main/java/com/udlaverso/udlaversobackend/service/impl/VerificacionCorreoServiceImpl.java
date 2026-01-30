package com.udlaverso.udlaversobackend.service.impl;

import com.udlaverso.udlaversobackend.entity.VerificacionCorreo;
import com.udlaverso.udlaversobackend.repository.VerificacionCorreoRepository;
import com.udlaverso.udlaversobackend.service.VerificacionCorreoService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificacionCorreoServiceImpl implements VerificacionCorreoService {

    private final VerificacionCorreoRepository repo;
    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String remitente;

    @Override
    public void enviarCodigo(String correo, String tipo) {
        LocalDateTime now = LocalDateTime.now();
        int baseIntentos = 0;
        var ultimo = repo.findTopByCorreoVerificacionCorreoAndTipoVerificacionCorreoOrderByExpiracionVerificacionCorreoDesc(correo, tipo);

        // Si ya hay un registro previo, verificamos los límites
        if (ultimo.isPresent()) {
            var anterior = ultimo.get();

            // Bloqueo activo
            if (anterior.getBloqueoHastaVerificacionCorreo() != null &&
                    anterior.getBloqueoHastaVerificacionCorreo().isAfter(now)) {
                throw new RuntimeException("Has alcanzado el número máximo de intentos. Intenta nuevamente después de " +
                        anterior.getBloqueoHastaVerificacionCorreo().toLocalTime() + ".");
            }

            // Cooldown (1 minuto entre envíos)
            if (anterior.getFechacreacionVerificacionCorreo() != null &&
                    anterior.getFechacreacionVerificacionCorreo().isAfter(now.minusSeconds(60))) {
                throw new RuntimeException("Debes esperar al menos 1 minuto antes de solicitar otro código.");
            }

            // Incrementar intentos y aplicar bloqueo si excede el límite
            boolean dentroVentana = anterior.getFechacreacionVerificacionCorreo() != null
                    && anterior.getFechacreacionVerificacionCorreo().isAfter(now.minusMinutes(30));
            baseIntentos = dentroVentana ? anterior.getIntentosVerificacionCorreo() : 0;
            int intentos = baseIntentos + 1;
            if (intentos > 5) {
                anterior.setBloqueoHastaVerificacionCorreo(now.plusMinutes(15)); // Bloquear 15 min
                repo.save(anterior);
                throw new RuntimeException("Has alcanzado el máximo de intentos. Intenta de nuevo más tarde.");
            }
        }

        // Crear nuevo código
        String codigo = String.format("%06d", new Random().nextInt(999999));
        VerificacionCorreo ver = new VerificacionCorreo();
        ver.setCorreoVerificacionCorreo(correo);
        ver.setCodigoVerificacionCorreo(codigo);
        ver.setTipoVerificacionCorreo(tipo);
        ver.setExpiracionVerificacionCorreo(now.plusMinutes(10));
        ver.setVerificadoVerificacionCorreo(false);
        ver.setUsadoVerificacionCorreo(false);
        ver.setFechacreacionVerificacionCorreo(now);
        ver.setIntentosVerificacionCorreo(baseIntentos + 1);

        repo.save(ver);

        // Envío del correo
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
        email.setFrom(remitente);
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

        if ("registro".equalsIgnoreCase(tipo)) {
            // En el registro, el código se marca como usado inmediatamente
            ver.setUsadoVerificacionCorreo(true);
        } else if ("recuperacion".equalsIgnoreCase(tipo)) {
            // En recuperación, solo se marca como verificado,
            // pero se dejará "usado" para cuando cambie la contraseña
            ver.setUsadoVerificacionCorreo(false);
        }

        repo.save(ver);
        return true;
    }
}
