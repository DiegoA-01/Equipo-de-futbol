package com.diego.futbol.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.diego.futbol.dto.Request.TrainingsRequestDTO;
import com.diego.futbol.dto.Response.DeleteUsersResponseDTO;
import com.diego.futbol.dto.Response.TrainingsResponseDTO;
import com.diego.futbol.entity.Trainings;
import com.diego.futbol.repository.TrainingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingsService {
    
    public final TrainingsRepository trainingsRepository;

    /**
     * Crear entrnamiento
     * 
     * @param request
     * @return
     */
    public TrainingsResponseDTO createdTraining(TrainingsRequestDTO request){

        if (trainingsRepository.existsByTrainingDate(request.getTrainingDate())) {
            throw new RuntimeException("Ya existe un entrenamiento con esa fecha.");
        }

        Trainings trainings = new Trainings();

        trainings.setTrainingDate(request.getTrainingDate());

        Trainings saveTraining = trainingsRepository.save(trainings);

        return toResponse(saveTraining);
    }

    /**
     * Listar entrenamientos creados 
     * 
     * @return
     */
    public List<TrainingsResponseDTO> listTraining(){

        return trainingsRepository.findAll().stream().map(this:: toResponse).toList();
    }

    /**
     * Buscar por ID 
     * 
     * @param trainingsId
     * @return
     */
    public TrainingsResponseDTO trainingId(Long trainingsId){

        Trainings trainings = trainingsRepository.findById(trainingsId).orElseThrow(()-> new RuntimeException("Entrenamiento no encontrado"));

        return toResponse(trainings);
    }


    /**
     * Metodo de actualizar por Id
     * 
     * @param trainingsId
     * @param request
     * @return
     */
    public TrainingsResponseDTO putTrainingId(Long trainingsId, TrainingsRequestDTO request){

        Trainings trainings = trainingsRepository.findById(trainingsId).orElseThrow(()-> new RuntimeException("Entrenamiento no encontrado"));

        trainings.setTrainingDate(request.getTrainingDate());

        Trainings savedTraining = trainingsRepository.save(trainings);

        return toResponse(savedTraining);
    }

    /**
     * Metodo de eliminar por id
     * 
     * @param trainingsId
     * @return
     */
    public DeleteUsersResponseDTO deleteId(Long trainingsId){
        trainingsRepository.findById(trainingsId).orElseThrow(()-> new RuntimeException("Entrenamiento no encontrado"));
        trainingsRepository.deleteById(trainingsId);

        return DeleteUsersResponseDTO.builder()
                .message("Usuario eliminado correctamente.")
                .build();
    }

    /**
     *  Convierte una entidad de trainings a TrainingsResponseDTO
     * Para retornar la informacion del usuario al cliente.
     * @param trainings
     * @return
     */
    public TrainingsResponseDTO toResponse(Trainings trainings){
        return TrainingsResponseDTO.builder()
                .trainingsId(trainings.getTrainingsId())
                .trainingDate(trainings.getTrainingDate())
                .createdAt(trainings.getCreatedAt())
                .isActive(trainings.getIsActive())
                .build();
    }
}
