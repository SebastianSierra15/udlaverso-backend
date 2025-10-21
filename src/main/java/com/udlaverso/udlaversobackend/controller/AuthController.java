package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import com.udlaverso.udlaversobackend.repository.RolRepository;
import com.udlaverso.udlaversobackend.security.JwtTokenProvider;
import com.udlaverso.udlaversobackend.entity.Usuario;
import com.udlaverso.udlaversobackend.dto.RegistroUsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        var user = usuarioRepo.findByCorreoUsuario(body.get("correo")).orElseThrow();
        var role = user.getRolUsuario().getNombreRol() != null ? user.getRolUsuario().getNombreRol() : "USER";
        String token = jwt.generate(user.getCorreoUsuario(), role);
        return Map.of("token", token, "role", role);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody RegistroUsuarioDTO dto) {
        if (usuarioRepo.existsByCorreoUsuario(dto.getCorreoUsuario())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo ya está registrado"));
        }

        var nuevoUsuario = new Usuario();
        nuevoUsuario.setNombresUsuario(dto.getNombresUsuario());
        nuevoUsuario.setApellidosUsuario(dto.getApellidosUsuario());
        nuevoUsuario.setCorreoUsuario(dto.getCorreoUsuario());
        nuevoUsuario.setContraseniaUsuario(encoder.encode(dto.getContraseniaUsuario()));
        nuevoUsuario.setUniversidadUsuario(dto.getUniversidadUsuario() != null ? dto.getUniversidadUsuario() : "Universidad de la Amazonia");
        nuevoUsuario.setEstadoUsuario((byte) 1);
        nuevoUsuario.setFechacreacionUsuario(LocalDateTime.now());

        // Asignar rol según dominio del correo
        String correo = dto.getCorreoUsuario();
        if (correo != null && correo.toLowerCase().endsWith("@udla.edu.co")) {
            rolRepo.findById(2).ifPresent(nuevoUsuario::setRolUsuario); // Institucional
        } else {
            rolRepo.findById(3).ifPresent(nuevoUsuario::setRolUsuario); // Externo
        }

        usuarioRepo.save(nuevoUsuario);

        return ResponseEntity.ok(Map.of(
                "mensaje", "Usuario registrado correctamente",
                "correo", nuevoUsuario.getCorreoUsuario()
        ));
    }
}