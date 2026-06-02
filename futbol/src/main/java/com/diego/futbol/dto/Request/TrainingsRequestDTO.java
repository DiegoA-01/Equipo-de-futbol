package com.diego.futbol.dto.Request;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class TrainingsRequestDTO {
    
    /**
     * Fecha en la cual se hace el entrenamiento 
     */
    @NotNull(message = "La fecha del entrenamiento es obligatoria")
    @Column(unique = true)
    private LocalDate trainingDate;
}
