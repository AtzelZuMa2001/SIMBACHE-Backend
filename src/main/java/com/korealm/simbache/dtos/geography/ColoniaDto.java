package com.korealm.simbache.dtos.geography;

import com.korealm.simbache.models.Colonia;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ColoniaDto {
    public ColoniaDto(Colonia colonia) {
        this.coloniaId = colonia.getColoniaId();
        this.coloniaName = colonia.getColoniaName();
    }

    @NotBlank
    private Long coloniaId;

    @NotBlank
    private String coloniaName;
}
