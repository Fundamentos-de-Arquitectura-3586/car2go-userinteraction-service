package com.pe.platform.shared.infrastructure.security;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenUtil {
    
    /**
     * Obtiene el JWT token del contexto de la petición actual
     * @return JWT token como String, o null si no se encuentra
     */
    public static String getCurrentJwtToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            return (String) request.getAttribute("JWT_TOKEN");
        } catch (IllegalStateException e) {
            // No hay contexto de petición activo
            return null;
        }
    }
    
    /**
     * Obtiene el JWT token formateado para header Authorization
     * @return JWT token con prefijo "Bearer ", o null si no se encuentra
     */
    public static String getCurrentJwtTokenWithBearer() {
        String token = getCurrentJwtToken();
        return token != null ? "Bearer " + token : null;
    }
}
