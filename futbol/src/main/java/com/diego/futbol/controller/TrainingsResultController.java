package com.diego.futbol.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.futbol.dto.Request.TrainingsResultRequestDTO;
import com.diego.futbol.dto.Response.ApiResponse;
import com.diego.futbol.dto.Response.TrainingsResultResponseDTO;
import com.diego.futbol.enums.Rol;
import com.diego.futbol.security.RequiresRole;
import com.diego.futbol.service.TrainingsResultService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainings-results")
@RequiredArgsConstructor
@Validated
public class TrainingsResultController {
    
    private final TrainingsResultService trainingsResultService;

    /**
     * Crear resultado de entrenamiento - Solo COACH
     */
    @PostMapping
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<TrainingsResultResponseDTO>> createdResult(@Valid @RequestBody TrainingsResultRequestDTO request){
        TrainingsResultResponseDTO response = trainingsResultService.createdResult(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<TrainingsResultResponseDTO>builder()
                        .message("Resultado de entrenamiento creado correctamente")
                        .data(response)
                        .build()
        );
    }

    /**
     * Listar resultados
     * COACH ve todos los resultados
     * PLAYER ve solo los suyos
     */
    @GetMapping
    @RequiresRole({Rol.COACH, Rol.PLAYER})
    public ResponseEntity<ApiResponse<List<TrainingsResultResponseDTO>>> listResult(HttpServletRequest request){
        String authenticatedRole = (String) request.getAttribute("authenticatedRole");
        List<TrainingsResultResponseDTO> results;

        if (authenticatedRole.equals(Rol.COACH.name())) {
            // El COACH ve todos los resultados
            results = trainingsResultService.listResult();
        } else {
            // El PLAYER solo ve sus resultados
            Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");
            results = trainingsResultService.findResultsByUserId(authenticatedUserId);
        }

        return ResponseEntity.ok(
            ApiResponse.<List<TrainingsResultResponseDTO>>builder()
                        .message("Lista de entrenamientos")
                        .data(results)
                        .build()
        );
    }

    /**
     * Buscar resultado por ID
     * COACH puede ver cualquiera
     * PLAYER solo puede ver el suyo
     */
    @GetMapping("/{id}")
    @RequiresRole({Rol.COACH, Rol.PLAYER})
    public ResponseEntity<ApiResponse<TrainingsResultResponseDTO>> findById(
            @Valid @PathVariable Long id,
            HttpServletRequest request){
        
        TrainingsResultResponseDTO result = trainingsResultService.findById(id);
        
        String authenticatedRole = (String) request.getAttribute("authenticatedRole");
        Long authenticatedUserId = (Long) request.getAttribute("authenticatedUserId");

        // Si es PLAYER, validar que sea su resultado
        if (authenticatedRole.equals(Rol.PLAYER.name()) && !result.getUserId().equals(authenticatedUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.<TrainingsResultResponseDTO>builder()
                        .message("No tienes permiso para ver este resultado")
                        .build()
            );
        }

        return ResponseEntity.ok(
            ApiResponse.<TrainingsResultResponseDTO>builder()
                        .message("Resultado de entrenamiento encontrado")
                        .data(result)
                        .build()
        );
    }
}
