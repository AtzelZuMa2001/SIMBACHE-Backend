package com.korealm.simbache.dtos.vehicles;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleTypeDto {
    @NotBlank
    private short typeId;

    @NotBlank
    private String typeName;
}
