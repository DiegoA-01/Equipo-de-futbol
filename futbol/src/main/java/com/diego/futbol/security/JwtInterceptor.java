package com.diego.futbol.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.diego.futbol.enums.Rol;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    
    private final JwtFilter jwtFilter;

    /**
     * Este método se ejecuta antes de que el controlador maneje la petición.
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {

        // Extraigo el header de autorización
        String authHeader = request.getHeader("Authorization");

        // Verifico que venga el token y tenga el formato correcto
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, "Falta el token de autorización o el formato es incorrecto");
            return false;
        }

        // Saco solo el token, quitando el "Bearer "
        String token = authHeader.substring(7);

        // Valido el token usando mi JwtFilter
        if (!jwtFilter.validateToken(token)) {
            sendUnauthorizedResponse(response, "El token es inválido o ya expiró");
            return false;
        }

        // Extraigo los datos del usuario y los guardo en el request
        String email = jwtFilter.extractEmail(token);
        String roleStr = jwtFilter.extractRole(token);
        Long userId = jwtFilter.extractUserId(token);
        Rol userRole = Rol.valueOf(roleStr);

        request.setAttribute("authenticatedEmail", email);
        request.setAttribute("authenticatedRole", roleStr);
        request.setAttribute("authenticatedUserId", userId);

        // Verifico permisos con la anotación @RequiresRole
        if (handler instanceof HandlerMethod handlerMethod) {
            RequiresRole requiresRole = handlerMethod.getMethodAnnotation(RequiresRole.class);
            
            if (requiresRole != null) {
                boolean hasPermission = Arrays.asList(requiresRole.value()).contains(userRole);
                
                if (!hasPermission) {
                    sendForbiddenResponse(response, "No tienes permisos suficientes para esta acción");
                    return false;
                }
            }
        }

        // Todo en orden, dejo pasar la petición
        return true;
    }

    /**
     * Envía una respuesta JSON con error 401 No autorizado
     * 
     * @param response
     * @param message
     * @throws IOException
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = String.format(
            "{\"status\": 401, \"error\": \"No autorizado\", \"message\": \"%s\"}",
            message
        );

        response.getWriter().write(jsonResponse);
    }

    /**
     * Envía una respuesta JSON con error 403 Acceso denegado
     * 
     * @param response
     * @param message
     * @throws IOException
     */
    private void sendForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        String jsonResponse = String.format(
            "{\"status\": 403, \"error\": \"Acceso denegado\", \"message\": \"%s\"}",
            message
        );

        response.getWriter().write(jsonResponse);
    }
}
