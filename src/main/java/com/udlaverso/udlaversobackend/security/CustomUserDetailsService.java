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
        var u = usuarioRepo.findByCorreoUsuario(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String role = (u.getRolUsuario() != null && u.getRolUsuario().getNombreRol() != null)
                ? u.getRolUsuario().getNombreRol()
                : "USER";

        return User.withUsername(u.getCorreoUsuario())
                .password(u.getContraseniaUsuario())
                .roles(role)
                .build();
    }
}
