package com.diego.futbol.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.diego.futbol.enums.Rol;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    
    /**
     * Lista de roles permitidos para acceder al endpoint.
     * Si no se especifica, por defecto requerirá COACH.
     */
    Rol[] value() default {Rol.COACH};
}

