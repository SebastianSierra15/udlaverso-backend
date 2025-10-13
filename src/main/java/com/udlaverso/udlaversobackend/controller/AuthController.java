package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import com.udlaverso.udlaversobackend.repository.RolRepository;
import com.udlaverso.udlaversobackend.security.JwtTokenProvider;
import com.udlaverso.udlaversobackend.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwt;
    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder encoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.get("correo"), body.get("contrasenia")));
        var user = usuarioRepo.findByCorreo(body.get("correo")).orElseThrow();
        var role = user.getRol() != null ? user.getRol().getNombre() : "USER";
        String token = jwt.generate(user.getCorreo(), role);
        return Map.of("token", token, "role", role);
    }

    @PostMapping("/registro")
    public Map<String, Object> registro(@RequestBody Map<String, String> body) {
        if (usuarioRepo.existsByCorreo(body.get("correo"))) throw new IllegalArgumentException("Correo ya usado");
        var u = new Usuario();
        u.setCorreo(body.get("correo"));
        u.setContrasenia(encoder.encode(body.get("contrasenia")));
        u.setNombres(body.get("nombres"));
        u.setApellidos(body.get("apellidos"));
        u.setUniversidad(body.get("universidad"));
        u.setEstado((byte) 1);
        u.setFechaCreacion(LocalDateTime.now());
        rolRepo.findById(1).ifPresent(u::setRol); // asume 1=USER
        usuarioRepo.save(u);
        return Map.of("id", u.getId(), "correo", u.getCorreo());
    }
}