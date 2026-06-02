package com.diego.futbol.entity;

import java.util.ArrayList;
import java.util.List;

import com.diego.futbol.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    /**
     * nombre del jugador o entrenador
     */
    @Column(name = "name")
    private String name;

    /**
     * apellido
     */
    @Column(name = "surname")
    private String surname;

    /**
     * Correo unico que va ser el subject
     */
    @Column(name = "email", unique = true)
    private String email;

    /**
     * contraseña del suaurio
     */
    @Column(name = "password")
    private String password;

    /**
     * Telefono que no puede ser repetido 
     */
    @Column(name = "phone", unique = true)
    private String phone;

    /**
     * Rol del usuario
     * Se guarda como texto y no como numero
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol role;

    /**
     * Estado del usuyariop que por defecto es activo
     */
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * Un jugador puede tener muchos resultados 
     */
    @Builder.Default
    @OneToMany(mappedBy = "users")
    private List<TrainingsResult> trainingsResults = new ArrayList<>();

}
