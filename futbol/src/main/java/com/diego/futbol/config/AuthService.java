package com.diego.futbol.config;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.diego.futbol.dto.Request.LoginRequestDTO;
import com.diego.futbol.dto.Request.RegisterRequestDTO;
import com.diego.futbol.dto.Response.LoginResponseDTO;
import com.diego.futbol.dto.Response.MessageResponseDTO;
import com.diego.futbol.entity.Users;
import com.diego.futbol.enums.Rol;
import com.diego.futbol.repository.UsersRepository;
import com.diego.futbol.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    /**
     * Registra un nuevo usuario
     * Por defecto, se registra como PLAYER
     * 
     * @param request datos de registro
     * @return mensaje de confirmación
     */
    public MessageResponseDTO register(RegisterRequestDTO request){
        
        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya existe, ingresa otro correo.");
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Si viene un rol en la request, se usa; si no, por defecto es PLAYER
        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole(Rol.PLAYER);
        }

        usersRepository.save(user);
        
        return MessageResponseDTO.builder()
                .message("Registro exitoso como " + user.getRole().name())
                .build();
    }

    /**
     * Inicia sesión con email y contraseña
     * 
     * @param request email y contraseña
     * @return token JWT
     */
    public LoginResponseDTO login(LoginRequestDTO request){
        Optional<Users> userOpt = usersRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || request.getEmail() == null) {
            throw new RuntimeException("Email inválido o usuario no encontrado");
        }

        Users userFound = userOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), userFound.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String jwt = jwtService.generatedToken(userFound);
        return LoginResponseDTO.builder()
                .jwt(jwt)
                .message("Inicio de sesión exitoso")
                .build();
    }

    /**
     * Refresca el token JWT
     * 
     * @param token token a refrescar
     * @return nuevo token
     * @throws Exception si el token es inválido o expiró
     */
    public LoginResponseDTO refreshToken(String token) throws Exception{
        String jwt = jwtService.refreshToken(token);
        return LoginResponseDTO.builder()
                .jwt(jwt)
                .message("Token refrescado exitosamente")
                .build();
    }
}
