package com.udlaverso.udlaversobackend.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class JwtAuthFilter extends GenericFilter {

    private final JwtTokenProvider jwt;
    private final UserDetailsService uds;

    public JwtAuthFilter(JwtTokenProvider jwt, UserDetailsService uds) {
        this.jwt = jwt;
        this.uds = uds;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        // Si la ruta es pública, no validar token
        if (uri.startsWith("/proyectos") ||
                uri.startsWith("/categorias") ||
                uri.startsWith("/auth") ||
                uri.startsWith("/noticias") ||
                uri.startsWith("/faqs") ||
                uri.startsWith("/swagger-ui") ||
                uri.startsWith("/v3/api-docs")) {
            chain.doFilter(request, response);
            return;
        }

        // Si no hay header Authorization, seguir sin autenticar
        String header = req.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
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

        chain.doFilter(request, response);
    }
}
