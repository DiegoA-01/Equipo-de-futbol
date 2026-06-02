package com.diego.futbol.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDate;

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
public class TrainingsResultResponseDTO {
    
    private Long id;

    private Long userId;

    private String playerName;

    private LocalDate trainigDate;

    private Long trainingsId;

    private BigDecimal shootingPower;

    private BigDecimal speed;

    private Integer effectivePasses;

    private BigDecimal score;
}
