package com.diego.futbol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.diego.futbol.security.JwtInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
     private final JwtInterceptor jwtInterceptor;

     /**
      * Agrega el interceptor de JWT a todas las rutas excepto las de autenticación (registro y login)
      * para validar el token en cada solicitud y asegurar que el usuario esté autenticado antes de acceder a los recursos protegidos.
      * Si el token es inválido o falta, el interceptor responderá con un error 401 Unauthorized y no permitirá el acceso al recurso solicitado.
      * Esto garantiza que solo los usuarios autenticados puedan acceder a las rutas protegidas de la aplicación, mientras que las rutas de registro y login permanecen accesibles para todos los usuarios.
      */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                    "http://localhost:4200",
                    "https://futbol-app-latest.onrender.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/v1/auth/register",
                        "/api/v1/auth/login"
                );
    }
}
