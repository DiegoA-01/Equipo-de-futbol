package com.diego.futbol.controller;

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

import com.diego.futbol.dto.Request.TrainingsRequestDTO;
import com.diego.futbol.dto.Response.ApiResponse;
import com.diego.futbol.dto.Response.TrainingsResponseDTO;
import com.diego.futbol.enums.Rol;
import com.diego.futbol.security.RequiresRole;
import com.diego.futbol.service.TrainingsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Validated
public class TrainingsController {

    private final TrainingsService trainingsService;

    /**
     * Crear entrenamiento - Solo COACH
     */
    @PostMapping
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<TrainingsResponseDTO>> createdTraining(@Valid @RequestBody TrainingsRequestDTO request){
        TrainingsResponseDTO response = trainingsService.createdTraining(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<TrainingsResponseDTO>builder()
                        .message("Entrenamiento Creado correctamente")
                        .data(response)
                        .build()
        );
    }

    /**
     * Listar entrenamientos - COACH y PLAYER
     */
    @GetMapping
    @RequiresRole({Rol.COACH, Rol.PLAYER})
    public ResponseEntity<ApiResponse<List<TrainingsResponseDTO>>> listTraining(){
        List<TrainingsResponseDTO> trainig = trainingsService.listTraining();

        return ResponseEntity.ok(
            ApiResponse.<List<TrainingsResponseDTO>>builder()
                        .message("Lista de entrenamientos")
                        .data(trainig)
                        .build()
        );
    }

    /**
     * Ver entrenamiento por ID - COACH y PLAYER
     */
    @GetMapping("/{trainingsId}")
    @RequiresRole({Rol.COACH, Rol.PLAYER})
    public ResponseEntity<ApiResponse<TrainingsResponseDTO>> trainingId(@Valid @PathVariable Long trainingsId){
        TrainingsResponseDTO trainings = trainingsService.trainingId(trainingsId);

        return ResponseEntity.ok(
            ApiResponse.<TrainingsResponseDTO>builder()
                        .message("Entrenamiento encontrado correctamente")
                        .data(trainings)
                        .build()
        );
    }

    /**
     * Actualizar entrenamiento - Solo COACH
     */
    @PutMapping("/{trainingsId}")
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<TrainingsResponseDTO>> putTrainingId(@Valid @PathVariable Long trainingsId, TrainingsRequestDTO request){
        TrainingsResponseDTO update = trainingsService.putTrainingId(trainingsId, request);

        return ResponseEntity.ok(
            ApiResponse.<TrainingsResponseDTO>builder()
                        .message("Entrenamiento actualizado correctamente")
                        .data(update)
                        .build()
        );
    }

    /**
     * Eliminar entrenamiento - Solo COACH
     */
    @DeleteMapping("/{trainingsId}")
    @RequiresRole(Rol.COACH)
    public ResponseEntity<ApiResponse<TrainingsResponseDTO>> deleteId(@Valid @PathVariable Long trainingsId){
        trainingsService.deleteId(trainingsId);

        return ResponseEntity.ok(
            ApiResponse.<TrainingsResponseDTO>builder()
                        .message("Entrenamiento eliminado correctamente")
                        .build()
        );
    }

}
