package com.diego.futbol.controller;

import com.diego.futbol.service.UsersService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.futbol.dto.Request.UsersRequestDTO;
import com.diego.futbol.dto.Response.ApiResponse;
import com.diego.futbol.dto.Response.UsersResponseDTO;
import com.diego.futbol.enums.Rol;
import com.diego.futbol.security.RequiresRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UsersController {
    
    private final UsersService usersService;

    /**
     * Crear nuevo usuario - Solo COACH
     */
    @PostMapping
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<UsersResponseDTO>> createdUser(@Valid @RequestBody UsersRequestDTO request){

        UsersResponseDTO response = usersService.createdUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<UsersResponseDTO>builder()
                    .message("Usuario creado exitosamente")
                    .data(response)
                    .build()
        );
    }

    /**
     * Listar todos los usuarios - Solo COACH
     */
    @GetMapping
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<List<UsersResponseDTO>>> listUsers(){
        List<UsersResponseDTO> users = usersService.listUsers();

        return ResponseEntity.ok(
            ApiResponse.<List<UsersResponseDTO>>builder()
                    .message("Lista de usuarios obtenida correctamente")
                    .data(users)
                    .build()
        );
    }

    /**
     * Ver usuario por ID
     * COACH puede ver cualquiera
     * PLAYER solo puede ver el suyo propio
     */
    @GetMapping("/{userId}")
    @RequiresRole({Rol.COACH, Rol.PLAYER})
    public ResponseEntity<ApiResponse<UsersResponseDTO>> showId(
            @Valid @PathVariable Long userId,
            HttpServletRequest request){
        
        // Validación de permisos
        Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
        String authenticatedRole = (String) request.getAttribute("authenticatedRole");
        
        /**
         * Si el rol es PLAYER y el ID del usuario autenticado no coincide con el ID solicitado, se deniega el acceso
          * Esto asegura que un PLAYER solo pueda ver su propio perfil
          * Mientras que un COACH puede ver cualquier perfil sin restricciones
         */
        if (authenticatedRole.equals(Rol.PLAYER.name()) && !userId.equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.<UsersResponseDTO>builder()
                        .message("No tienes permiso para ver el perfil de otro usuario")
                        .build()
            );
        }
        
        UsersResponseDTO user = usersService.showId(userId);

        return ResponseEntity.ok(
            ApiResponse.<UsersResponseDTO>builder()
                    .message("Usuario encontrado correctamente")
                    .data(user)
                    .build()
        );
    }

    /**
     * Actualizar usuario
     * COACH puede actualizar cualquiera
     * PLAYER solo puede actualizar el suyo propio
     */
    @PutMapping("/{userId}")
    @RequiresRole({Rol.COACH, Rol.PLAYER})
    public ResponseEntity<ApiResponse<UsersResponseDTO>> updateId(
            @PathVariable Long userId, 
            @RequestBody UsersRequestDTO request,
            HttpServletRequest httpRequest){
        
        // Validación de permisos
        /**
         * Si el rol es PLAYER y el ID del usuario autenticado no coincide con el ID solicitado, se deniega el acceso
         * Esto asegura que un PLAYER solo pueda actualizar su propio perfil
         */
        Long authenticatedUserId = (Long) httpRequest.getAttribute("authenticatedUserId");
        String authenticatedRole = (String) httpRequest.getAttribute("authenticatedRole");
        
        if (authenticatedRole.equals(Rol.PLAYER.name()) && !userId.equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.<UsersResponseDTO>builder()
                        .message("No tienes permiso para actualizar el perfil de otro usuario")
                        .build()
            );
        }
        
        UsersResponseDTO update = usersService.updateId(userId, request);
        
        return ResponseEntity.ok(
            ApiResponse.<UsersResponseDTO>builder()
                    .message("Usuario actualizado correctamente")
                    .data(update)
                    .build()
        );
    }

    /**
     * Eliminar usuario - Solo COACH
     */
    @DeleteMapping("/{userId}")
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<UsersResponseDTO>> deleteUseresId(@PathVariable Long userId){
        usersService.deleteUseresId(userId);

        return ResponseEntity.ok(
            ApiResponse.<UsersResponseDTO>builder()
                    .message("Usuario eliminado correctamente")
                    .build()
        );
    }
}
