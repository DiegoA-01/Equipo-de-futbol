package com.diego.futbol.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.diego.futbol.dto.Request.TrainingsResultRequestDTO;
import com.diego.futbol.dto.Response.TrainingsResultResponseDTO;
import com.diego.futbol.entity.Trainings;
import com.diego.futbol.entity.TrainingsResult;
import com.diego.futbol.entity.Users;
import com.diego.futbol.repository.TrainingsRepository;
import com.diego.futbol.repository.TrainingsResultRepository;
import com.diego.futbol.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingsResultService {
    
    private final TrainingsResultRepository trainingsResultRepository;
    private final UsersRepository usersRepository;
    private final TrainingsRepository trainingsRepository;

    /**
     * Registrar resultado de un entrenamiento
     * 
     * @param request
     * @return
     */
    public TrainingsResultResponseDTO createdResult(TrainingsResultRequestDTO request){
        Users users = usersRepository.findById(request.getUserId()).orElseThrow(()-> new RuntimeException("Jugador no encontrado"));

        Trainings trainings = trainingsRepository.findById(request.getTrainingsId()).orElseThrow(()-> new RuntimeException("Entrenamiento no encontrado."));

        BigDecimal score = calculateScore(
            request.getShootingPower(),
            request.getSpeed(),
            request.getEffectivePasses()
        );

        TrainingsResult result = new TrainingsResult();

        result.setUsers(users);
        result.setTrainings(trainings);
        result.setShootingPower(request.getShootingPower());
        result.setSpeed(request.getSpeed());
        result.setEffectivePasses(request.getEffectivePasses());
        result.setScore(score);

        TrainingsResult savedResult = trainingsResultRepository.save(result);

        return toResponse(savedResult);
    }


    /**
     * Listar todos los resultados
     * 
     * @return
     */
    public List<TrainingsResultResponseDTO> listResult(){
        return trainingsResultRepository.findAll().stream().map(this :: toResponse).toList();
    }


    /**
     * Buscar un resultado por ID
     * 
     * @param id
     * @return
     */
    public TrainingsResultResponseDTO findById(Long id){
        TrainingsResult result = trainingsResultRepository.findById(id).orElseThrow(()-> new RuntimeException("Resultado no encontrado"));

        return toResponse(result);
    }


    /**
     * Buscar resultados de un usuario específico
     * Los PLAYERS solo verán sus propios resultados
     * Los COACHES verán todos los resultados
     * 
     * @param userId ID del usuario
     * @return Lista de resultados del usuario
     */
    public List<TrainingsResultResponseDTO> findResultsByUserId(Long userId){
        return trainingsResultRepository.findByUsersUserId(userId)
                .stream()
                .map(this :: toResponse)
                .toList();
    }


    /**
     * Calcula el puntaje del jugador
     * 
     * potencia de tiro = 20%
     * velocidad = 30%
     * pases efectivos = 50%
     * 
     * @param shootingPower
     * @param speed
     * @param effectivePasses
     * @return
     */
    private BigDecimal calculateScore(BigDecimal shootingPower, BigDecimal speed, Integer effectivePasses){

        return shootingPower.multiply(BigDecimal.valueOf(0.20))
                .add(speed.multiply(BigDecimal.valueOf(0.30)))
                .add(BigDecimal.valueOf(effectivePasses).multiply(BigDecimal.valueOf(0.50)));
    }




    /**
     * Convierte la entidad en un DTO de respuesta
     * Para darle una respuesta al cliente
     * 
     * @param trainingsResult
     * @return
     */
    public TrainingsResultResponseDTO toResponse(TrainingsResult trainingsResult){
        return TrainingsResultResponseDTO.builder()
                .id(trainingsResult.getId())
                .userId(trainingsResult.getUsers().getUserId())
                .playerName(trainingsResult.getUsers().getName())
                .trainingsId(trainingsResult.getTrainings().getTrainingsId())
                .trainigDate(trainingsResult.getTrainings().getTrainingDate())
                .shootingPower(trainingsResult.getShootingPower())
                .speed(trainingsResult.getSpeed())
                .effectivePasses(trainingsResult.getEffectivePasses())
                .score(trainingsResult.getScore())
                .build();
    }

}
