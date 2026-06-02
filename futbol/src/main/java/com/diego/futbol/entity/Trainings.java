package com.diego.futbol.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trainings {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long trainingsId;

    /**
     * Fecha real del entrnamiento entonces con esto se puede ver si existe los 3 dias para calcular la potencia
     *
     */
    @Column(name = "training_date", nullable = false, unique = true)
    private LocalDate trainingDate;

    /**
     * Fecha de creación automática
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Estado de entrenamineto que por defecto es activo
     */
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * Un entrenamiento puede tener muchos resultados
     */
    @Builder.Default
    @OneToMany(mappedBy = "trainings")
    private List<TrainingsResult> trainingsResults = new ArrayList<>();


}
