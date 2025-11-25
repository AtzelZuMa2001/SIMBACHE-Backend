package com.korealm.simbache.dtos.geography;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalityUpdateDto {
    @NotNull
    private Short localityId;

    private String localityName;
    private Short type;
    private Integer postalCode;
}
