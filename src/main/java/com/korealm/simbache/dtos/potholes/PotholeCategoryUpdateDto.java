package com.korealm.simbache.dtos.potholes;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotholeCategoryUpdateDto {

    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres.")
    private String name;

    @Size(max = 512, message = "La descripción no puede exceder 512 caracteres.")
    private String description;

    private Integer priorityLevel;
}