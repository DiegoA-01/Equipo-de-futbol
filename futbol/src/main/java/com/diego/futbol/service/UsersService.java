package com.diego.futbol.service;


import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.diego.futbol.dto.Request.UsersRequestDTO;
import com.diego.futbol.dto.Response.DeleteUsersResponseDTO;
import com.diego.futbol.dto.Response.UsersResponseDTO;
import com.diego.futbol.entity.Users;
import com.diego.futbol.enums.Rol;
import com.diego.futbol.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
    
    private final PasswordEncoder passwordEncoder;
    
    public final UsersRepository usersRepository;

    /**
     * Metodo de crear un usuario
     * Por defecto se crea como PLAYER
     * 
     * Validacion que no se pueda repetir el correo ni el telefono
     * 
     * @param request
     * @return
     */
    public UsersResponseDTO createdUser(UsersRequestDTO request){

        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya existe, ingresa otro correo.");
        }
        if (usersRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Telefono ya existe."); 
        }

        Users users = new Users();
        users.setName(request.getName());
        users.setSurname(request.getSurname());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setPhone(request.getPhone());
        
        // Por defecto se crea como PLAYER, solo un admin podría cambiar esto
        if (request.getRole() != null) {
            users.setRole(request.getRole());
        } else {
            users.setRole(Rol.PLAYER);
        }

        Users saveUsers = usersRepository.save(users);

        return toResponse(saveUsers);
    }

    /**
     * Listar todos los usuarios
     * Solo COACH puede listar todos
     * 
     * @return
     */
    public List<UsersResponseDTO> listUsers(){
        return usersRepository.findAll().stream().map(this :: toResponse).toList();
    }

    /**
     * Obtener usuario por ID
     * Un PLAYER solo puede ver su propio perfil
     * Un COACH puede ver cualquier perfil
     * 
     * @param userId
     * @return
     */
    public UsersResponseDTO showId(Long userId){

        Users users = usersRepository.findById(userId).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        return toResponse(users);
    }


    /**
     * Actualizar Usuario por ID
     * Un PLAYER solo puede actualizar su propio perfil
     * Un COACH puede actualizar cualquier perfil
     * 
     * @param userId
     * @param request
     * @return
     */
    public UsersResponseDTO updateId(Long userId, UsersRequestDTO request){
        Users users = usersRepository.findById(userId).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        users.setName(request.getName());
        users.setSurname(request.getSurname());
        users.setEmail(request.getEmail());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setPhone(request.getPhone());
        
        // Solo mantener el rol si es válido
        if (request.getRole() != null) {
            users.setRole(request.getRole());
        }

        Users saveUser = usersRepository.save(users);

        return toResponse(saveUser);
    }

    /**
     * Metodo para eliminar usuario
     * Solo COACH puede eliminar
     * 
     * @param userId
     * @return
     */
    public DeleteUsersResponseDTO deleteUseresId(Long userId){
        usersRepository.findById(userId).orElseThrow(()-> new RuntimeException("Usuario no encontrado."));
        usersRepository.deleteById(userId);

        return DeleteUsersResponseDTO.builder()
                .message("Usuario eliminado correctamente")
                .build();
    }

    /**
     * Convierte la entidad Users en un UsersResponseDTO
     * para retornar la informacion del usuario al cliente.
     * 
     * @param users
     * @return
     */
    public UsersResponseDTO toResponse(Users users){
        return UsersResponseDTO.builder()
                .userId(users.getUserId())
                .name(users.getName())
                .surname(users.getSurname())
                .email(users.getEmail())
                .password(users.getPassword())
                .phone(users.getPhone())
                .role(users.getRole().name())
                .isActive(users.getIsActive())
                .build();
    }
}
