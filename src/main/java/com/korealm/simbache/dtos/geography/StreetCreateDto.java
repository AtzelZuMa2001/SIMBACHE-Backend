package com.korealm.simbache.dtos.geography;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StreetCreateDto {
    @NotBlank
    private String streetName;

    @NotNull
    private Integer localityId;

    // optional association
    private Long coloniaId;
}
