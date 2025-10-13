package com.udlaverso.udlaversobackend.security;

import com.udlaverso.udlaversobackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        var u = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        var role = (u.getRol() != null && u.getRol().getNombre() != null) ? u.getRol().getNombre() : "USER";
        return User.withUsername(u.getCorreo())
                .password(u.getContrasenia())
                .roles(role) // convierte a ROLE_{role}
                .build();
    }
}
