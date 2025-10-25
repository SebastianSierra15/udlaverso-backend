package com.udlaverso.udlaversobackend.controller;

import com.udlaverso.udlaversobackend.entity.Permiso;
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
import com.udlaverso.udlaversobackend.service.VerificacionCorreoService;
import com.udlaverso.udlaversobackend.repository.VerificacionCorreoRepository;

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
    private final VerificacionCorreoService verificacionCorreoService;
    private final VerificacionCorreoRepository verificacionCorreoRepo;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.get("correo"), body.get("contrasenia")));
        var user = usuarioRepo.findByCorreoUsuario(body.get("correo")).orElseThrow();
        var role = user.getRolUsuario().getNombreRol() != null ? user.getRolUsuario().getNombreRol() : "USER";

        // 🧩 Agrega esto:
        System.out.println("===== LOGIN DEBUG =====");
        System.out.println("Usuario: " + user.getCorreoUsuario());
        System.out.println("Rol: " + role);
        System.out.println("Permisos cargados: " + user.getRolUsuario().getPermisosRol());
        System.out.println("========================");

        var permisos = user.getRolUsuario().getPermisosRol()
                .stream()
                .map(Permiso::getNombrePermiso)
                .toList();

        String token = jwt.generate(user.getCorreoUsuario(), role);
        return Map.of("token", token, "role", role, "permissions", permisos);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody RegistroUsuarioDTO dto) {
        if (usuarioRepo.existsByCorreoUsuario(dto.getCorreoUsuario())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo ya está registrado"));
        }


        var verificacion = verificacionCorreoRepo.findTopByCorreoVerificacionCorreoAndTipoVerificacionCorreoOrderByExpiracionVerificacionCorreoDesc(
                dto.getCorreoUsuario(), "registro"
        );
        if (verificacion.isEmpty() || !verificacion.get().isVerificadoVerificacionCorreo()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debe verificar su correo antes de registrarse"));
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

    @PostMapping("/enviar-codigo")
    public ResponseEntity<?> enviarCodigo(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String tipo = body.getOrDefault("tipo", "registro");

        try {
            if ("registro".equalsIgnoreCase(tipo)) {
                if (usuarioRepo.existsByCorreoUsuario(correo)) {
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "El correo ya está registrado. No puedes crear otra cuenta con este correo.")
                    );
                }
            } else if ("recuperacion".equalsIgnoreCase(tipo)) {
                if (!usuarioRepo.existsByCorreoUsuario(correo)) {
                    return ResponseEntity.badRequest().body(
                            Map.of("error", "No existe ninguna cuenta asociada a este correo.")
                    );
                }
            }

            verificacionCorreoService.enviarCodigo(correo, tipo);
            return ResponseEntity.ok(Map.of("mensaje", "Código enviado al correo."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String codigo = body.get("codigo");
        String tipo = body.getOrDefault("tipo", "registro");
        boolean valido = verificacionCorreoService.verificarCodigo(correo, codigo, tipo);
        if (!valido)
            return ResponseEntity.badRequest().body(Map.of("error", "Código inválido o expirado"));
        return ResponseEntity.ok(Map.of("mensaje", "Código verificado correctamente"));
    }

    @PostMapping("/restablecer-contrasenia")
    public ResponseEntity<?> restablecerContrasenia(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        String nueva = body.get("nuevaContrasenia");

        var verOpt = verificacionCorreoRepo.findTopByCorreoVerificacionCorreoAndTipoVerificacionCorreoOrderByExpiracionVerificacionCorreoDesc(correo, "recuperacion");

        if (verOpt.isEmpty() || !verOpt.get().isVerificadoVerificacionCorreo()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debe verificar el código antes de restablecer la contraseña."));
        }

        var verificacion = verOpt.get();
        if (verificacion.getExpiracionVerificacionCorreo().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El código ha expirado."));
        }

        var usuario = usuarioRepo.findByCorreoUsuario(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setContraseniaUsuario(encoder.encode(nueva));
        usuarioRepo.save(usuario);

        verificacion.setUsadoVerificacionCorreo(true);
        verificacionCorreoRepo.save(verificacion);

        return ResponseEntity.ok(Map.of("mensaje", "Contraseña restablecida correctamente."));
    }
}