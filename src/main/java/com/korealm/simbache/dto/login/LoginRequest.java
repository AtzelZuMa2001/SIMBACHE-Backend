package com.korealm.simbache.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {

    @NotBlank
    private String username;

    /* Aquí no recibimos la contraseña en texto plano, sino en hash (un hash es una cadena de texto encriptada)
        El frontend debe encargarse de encriptar la contraseña antes de enviarla al servidor. Esto SIEMPRE debe ser así
        para evitar hackeos.
     */
    @NotBlank
    private String passwordHash;
}
