package com.korealm.simbache.dtos.geography;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColoniaCreateDto {
    @NotBlank
    private String coloniaName;

    @NotNull
    private Integer localityId;
}
