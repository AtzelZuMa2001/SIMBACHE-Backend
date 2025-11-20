package com.korealm.simbache.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BasicUpdateDto {
    @NotBlank
    private String currentName;

    @NotBlank
    private String newName;
}
