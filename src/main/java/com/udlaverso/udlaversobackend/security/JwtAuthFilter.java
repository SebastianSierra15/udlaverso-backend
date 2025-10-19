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
        System.out.println("🔎 URI actual: " + uri);

        // Rutas públicas
        if (uri.startsWith("/proyectos") ||
                uri.startsWith("/categorias") ||
                uri.startsWith("/auth") ||
                uri.startsWith("/noticias") ||
                uri.startsWith("/faqs") ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs")) {
            chain.doFilter(req, res);
            return;
        }

        // Si hay token, validarlo
        String header = req.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
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
        }

        chain.doFilter(req, res);
    }
}
