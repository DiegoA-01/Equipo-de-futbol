package com.diego.futbol.dto.Request;


import com.diego.futbol.enums.Rol;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class UsersRequestDTO {
    
    /**
     * Nombre del usuario
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50,message = "El nombre mo puede tener mas de los 50 caracteres")
    private String name;

    /**
     * Apellido del usuario
     */
    @NotBlank(message = "El apellido debe ser obligatorio")
    @Size(max = 50, message = "El apellido no puede pasar de los 50 caracteres")
    private String surname;


    /**
     * Correo del usuario
     */
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido")
    private String email;

    /**
     * Contraseña del usuario
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Telefono del usuario
     */
    @NotBlank(message = "El telefono es obligatorio")
    private String phone;

    /**
     * El rol sea ENTRENADOR O JUGADOR
     */
    @NotNull(message = "El rol es obligatorio")
    private Rol role;

}
