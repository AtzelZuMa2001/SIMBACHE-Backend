package com.korealm.simbache.dto.login;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorResponse {
    private String message;
}