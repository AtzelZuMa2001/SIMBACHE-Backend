package com.korealm.simbache.dtos.geography;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationCreateDto {
    @NotNull
    private Short stateId;

    @NotNull
    private Short municipalityId;

    @NotNull
    private Integer localityId;

    @NotNull
    private Long mainStreetId;

    // nullable
    private Long streetOneId;

    // nullable
    private Long streetTwoId;
}
