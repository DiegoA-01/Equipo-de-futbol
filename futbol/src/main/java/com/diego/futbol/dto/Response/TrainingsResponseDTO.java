package com.diego.futbol.dto.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class TrainingsResponseDTO {
    
    private Long trainingsId;

    private LocalDate trainingDate;

    private LocalDateTime createdAt;

    private Boolean isActive;
}
