package com.diego.futbol.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.diego.futbol.dto.Response.TeamPlayerResponseDTO;
import com.diego.futbol.entity.TrainingsResult;
import com.diego.futbol.entity.Users;
import com.diego.futbol.repository.TrainingsRepository;
import com.diego.futbol.repository.TrainingsResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    
    private final TrainingsRepository trainingsRepository;
    private final TrainingsResultRepository trainingsResultRepository;

    /**
     * Obtiene el equipo titular de la semana
     * 
     * Valida que existan al menos tres entrenamientos
     * calcula el promedio de cada jugador y retorna los 5 jugadores con mejores puntuaciones
     * 
     * @return Lista de jugadores titulares
     */
    public List<TeamPlayerResponseDTO> getStartes(){

        validateThereeTrainings();

        List<TrainingsResult> results = trainingsResultRepository.findAll();
        
        Map<Long, List<TrainingsResult>> resultsByPlayer = groupResultsByPlayer(results);

        List<TeamPlayerResponseDTO> playerAverage = calculatePlayersAverage(resultsByPlayer);

        return playerAverage.stream()
                .sorted(Comparator.comparing(TeamPlayerResponseDTO :: getAverageScore).reversed()).limit(5).toList();
        
    }

 
    /**
     * Valida que existan al menos 3 entrenamientos registrados en el sistema
     * 
     * en esta prueba es necersario por que tienen que tener 3 entrenos en la semana 
     */
    private void validateThereeTrainings(){

        Long totalTrainings = trainingsRepository.count();

        if (totalTrainings < 3) {
            throw new RuntimeException("No hay suficiente informacion para generar el equipo titular.");
        }
    }

    /**
     * Agrupa los resultados por jugador
     * 
     * la llave del mapa corresponde al identificador del jugador 
     * y el valor contiene sus resultados
     * 
     * @param results Lista de resultados registrados
     * @return resultados agrupados por jugador 
     */
    private Map<Long, List<TrainingsResult>> groupResultsByPlayer(List<TrainingsResult> results){

        return results.stream()
                .collect(Collectors.groupingBy(result -> result.getUsers().getUserId()));
    }

    /**
     * Calcula el promedio de puntuacion para cada jugador 
     * 
     * recorre los resultados agrupados y construye un DTO
     * con la informacion  y el promedio semanal de cada jugador 
     * 
     * @param resultsByPlayer Resultados agrupados por jugador 
     * @return Lista de jugadores con su promedio calculado
     */
    private List<TeamPlayerResponseDTO> calculatePlayersAverage(Map<Long, List<TrainingsResult>> resultsByPlayer){

        return resultsByPlayer.values()
                .stream()
                .map(this :: buildPlayerAverage)
                .toList();
    }

    /**
     * Contruye la informacion estadistica de un jugador 
     * 
     * calcula el promedio de las puntuaciones obtenidas
     * en los entrenamientos y genera unDTO utilizado
     * para conformar el equipo titular
     * 
     * @param playerResults Lista de resultados de un jufgador
     * @return Informacion del jugador con su promedio semanal
     */
    private TeamPlayerResponseDTO buildPlayerAverage(List<TrainingsResult> playerResults){

        Users player = playerResults.get(0).getUsers();

        BigDecimal totalScore = playerResults.stream()
                    .map(TrainingsResult :: getScore)
                    .reduce(BigDecimal.ZERO, BigDecimal:: add);

        BigDecimal averageScore = totalScore.divide(BigDecimal.valueOf(playerResults.size()),
                    2,
                    RoundingMode.HALF_UP
         );  

        return TeamPlayerResponseDTO.builder()
                .playerId(player.getUserId())
                .playerName(player.getName() + " " + player.getSurname())
                .averageScore(averageScore)
                .build();
    }
}
