package com.pe.platform.shared.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.crypto.SecretKey;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Usa exactamente el mismo secret con el que generaste el token
    private static final String SECRET_KEY = "WriteHereYourSecretStringForTokenSigningCredentials"; // 32 caracteres

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Filtro JWT interceptó la petición");

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (Exception e) {
            // Firma inválida o token corrupto
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Object idClaim = claims.get("userId");
        if (idClaim == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        long userId;
        try {
            userId = Long.parseLong(idClaim.toString());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        var authToken = new UsernamePasswordAuthenticationToken(
                userId, null, Collections.emptyList()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Guardar el JWT en el contexto de la petición para uso posterior
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        attributes.getRequest().setAttribute("JWT_TOKEN", jwt);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}