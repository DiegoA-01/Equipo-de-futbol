package com.diego.futbol.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diego.futbol.entity.Trainings;

public interface TrainingsRepository extends JpaRepository<Trainings, Long>{
 
    boolean existsByTrainingDate(LocalDate trainingDate);
}
