package com.diego.futbol.security;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtFilter {
    
    private final JwtService jwtService;

    /**
     * Valida que el token sea válido
     */
    public boolean validateToken(String token) {
        return jwtService.validToken(token);
    }

    /**
     * Extrae el email del token
     */
    public String extractEmail(String token) {
        return jwtService.extractEmail(token);
    }

    /**
     * Extrae el rol del token
     */
    public String extractRole(String token) {
        return jwtService.extractRol(token);
    }

    /**
     * Extrae el userId del token
     */
    public Long extractUserId(String token) {
        return jwtService.extractUserId(token);
    }
}
