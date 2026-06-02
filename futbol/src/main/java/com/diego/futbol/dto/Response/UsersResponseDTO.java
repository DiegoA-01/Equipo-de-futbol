package com.diego.futbol.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponseDTO {
    
    private Long userId;

    private String name;

    private String surname;

    private String email;

    private String password;

    private String phone;

    private String role;

    private Boolean isActive;

}
