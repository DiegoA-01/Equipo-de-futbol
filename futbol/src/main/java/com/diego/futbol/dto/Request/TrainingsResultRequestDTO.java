package com.diego.futbol.dto.Request;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class TrainingsResultRequestDTO {
    
    /**
     * ID del jugador
     */
    @NotNull(message = "El usuario es obligatorio")
    private Long userId;

    /**
     * Id del entrenamiento 
     */
    @NotNull(message = "El entrenamiento es obligatorio")
    private Long TrainingsId;

    /**
     *Potecia de tiro
     */
    @NotNull(message = "La potencia de tiro es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La potencia de tiro debe ser mayor a 0")
    private BigDecimal shootingPower;

    /**
     * Velocidad del jugador
     */
    @NotNull(message = "La velocidad es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La velocidad debe ser mayor a 0")
    private BigDecimal speed;

    /**
     * Cantidad de pases efectivos 
     */
    @NotNull(message = "Los pases efectivos son obligatorios")
    @Min(value = 0, message = "Los pases efectivos no pueden ser efectivos")
    private Integer effectivePasses;
}
