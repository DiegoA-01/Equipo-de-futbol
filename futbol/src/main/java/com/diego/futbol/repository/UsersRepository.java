package com.diego.futbol.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.diego.futbol.entity.Users;

public interface UsersRepository extends JpaRepository<Users,Long>{

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email
     * @return
     */
    Optional<Users> findByEmail(String email);
    
}
