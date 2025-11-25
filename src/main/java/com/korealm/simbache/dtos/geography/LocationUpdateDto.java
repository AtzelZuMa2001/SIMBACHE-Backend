package com.korealm.simbache.dtos.geography;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationUpdateDto {
    @NotNull
    private Long locationId;

    // The following fields are optional; only provided ones will be updated
    private Short stateId;
    private Short municipalityId;
    private Integer localityId;
    private Long mainStreetId;
    private Long streetOneId; // nullable
    private Long streetTwoId; // nullable
}
