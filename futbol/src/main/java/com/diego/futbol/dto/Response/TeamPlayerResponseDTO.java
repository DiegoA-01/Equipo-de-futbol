package com.diego.futbol.dto.Response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayerResponseDTO {
    
    /**
     * Id del jugador
     */
    private Long playerId;

    /**
     * Nombre del jugador 
     */
    private String playerName;

    /**
     * Promedio Obtenido durante la semana
     */
    private BigDecimal averageScore;
}
