package com.diego.futbol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diego.futbol.entity.TrainingsResult;

public interface TrainingsResultRepository extends JpaRepository<TrainingsResult,Long>{
    
    /**
     * Encuentra todos los resultados de un usuario específico
     * @param userId ID del usuario
     * @return Lista de resultados del usuario
     */
    List<TrainingsResult> findByUsersUserId(Long userId);
}
