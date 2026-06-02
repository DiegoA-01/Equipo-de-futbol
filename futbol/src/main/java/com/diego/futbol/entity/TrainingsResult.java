package com.diego.futbol.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "training_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingsResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Muchos resultados pertenecen a un usuario
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    /**
     *  Muchos resultados pertenecen a un entrenamiento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Trainings trainings;

    /**
     * Potencia de tiro
     */
    @Column(name = "shooting_power", nullable = false, precision = 10, scale = 2)
    private BigDecimal shootingPower;

    /**
     * Velocidad del jugador
     */
    @Column(name = "speed", nullable = false, precision = 10, scale = 2)
    private BigDecimal speed;

    /**
     * Calidda de pases efectivos
     */
    @Column(name = "effective_passes", nullable = false)
    private Integer effectivePasses;

    /**
     * Puntaje final calculado
     */
    @Column(name = "score", nullable = false, precision = 10, scale = 2)
    private BigDecimal score;

    
}
