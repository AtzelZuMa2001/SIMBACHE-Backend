package com.korealm.simbache.dtos.vehicles;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleTypeUpdateDto {
    @NotBlank
    private String currentName;

    @NotBlank
    private String newName;
}
