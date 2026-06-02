package com.diego.futbol.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diego.futbol.config.AuthService;
import com.diego.futbol.dto.Request.LoginRequestDTO;
import com.diego.futbol.dto.Request.RegisterRequestDTO;
import com.diego.futbol.dto.Response.ApiResponse;
import com.diego.futbol.dto.Response.LoginResponseDTO;
import com.diego.futbol.dto.Response.MessageResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MessageResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO request){
        MessageResponseDTO response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<MessageResponseDTO>builder()
                    .message("Registro exitoso")
                    .data(response)
                    .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request){
        LoginResponseDTO response = authService.login(request);

        return ResponseEntity.ok(
            ApiResponse.<LoginResponseDTO>builder()
                    .message("Inicio de sesión exitoso")
                    .data(response)
                    .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refreshToken(@RequestBody String token) throws Exception {
        LoginResponseDTO response = authService.refreshToken(token);

        return ResponseEntity.ok(
            ApiResponse.<LoginResponseDTO>builder()
                    .message("Token refrescado exitosamente")
                    .data(response)
                    .build()
        );
    }
}
