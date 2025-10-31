package com.udlaverso.udlaversobackend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwt;
    private final UserDetailsService uds;

    public JwtAuthFilter(JwtTokenProvider jwt, UserDetailsService uds) {
        this.jwt = jwt;
        this.uds = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        String method = req.getMethod();
        System.out.println("🔎 URI actual: " + uri + " | Método: " + method);

        // === RUTAS PÚBLICAS (solo GET) ===
        if (method.equalsIgnoreCase("GET") && (
                uri.startsWith("/proyectos") ||
                        uri.startsWith("/categorias") ||
                        uri.startsWith("/noticias") ||
                        uri.startsWith("/faqs") ||
                        uri.startsWith("/uploads") ||
                        uri.startsWith("/swagger-ui") ||
                        uri.startsWith("/v3/api-docs") ||
                        uri.startsWith("/resenias/proyecto")
        ) || uri.startsWith("/auth") || uri.startsWith("/mail")) {
            chain.doFilter(req, res);
            return;
        }

        // === VALIDACIÓN JWT ===
        String header = req.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            System.out.println("🚫 Sin token JWT o formato inválido");
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String token = header.substring(7);
            String username = jwt.getUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = uds.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error JWT: " + e.getMessage());
            SecurityContextHolder.clearContext();
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(req, res);
    }
}
