package com.korealm.simbache.dtos.potholes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotholeCategoryCreateDto {

    @NotNull(message = "El id de la categoría es requerido.")
    private Short categoryId;

    @NotBlank(message = "El nombre de la categoría es requerido.")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres.")
    private String name;

    @NotBlank(message = "La descripción es requerida.")
    @Size(max = 512, message = "La descripción no puede exceder 512 caracteres.")
    private String description;

    @NotNull(message = "El nivel de prioridad es requerido.")
    private Integer priorityLevel;
}