package com.diego.futbol.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.futbol.dto.Response.TeamPlayerResponseDTO;
import com.diego.futbol.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;

    /**
     * Obtiene los 5 jugadores titulares de la semana 
     * 
     * el equipo se generea a partir del promedio de los resultados obtenidos en los entrenamientos
     * registrados
     * 
     * @return Lista de los 5 jugadores titulares
     */
    @GetMapping("/starters")
    public ResponseEntity<List<TeamPlayerResponseDTO>> getStarters(){

        List<TeamPlayerResponseDTO> starters = teamService.getStartes();

        return ResponseEntity.ok(starters);
    }
}
