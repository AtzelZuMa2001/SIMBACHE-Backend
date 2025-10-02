package com.korealm.simbache.dtos.login;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;   // token o identificador único en la sesión del usuario (UUID string)
    private String firstName;
    private String lastName;
}