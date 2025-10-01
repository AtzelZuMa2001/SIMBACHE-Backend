package com.korealm.simbache.dto.login;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;   // session token (UUID string)

    // userId nunca va a ser nulo porque todas las respuestas de login exitosas que enviemos de regreso, lo tienen.
    @NotNull
    private Long userId;
    private String username;
}