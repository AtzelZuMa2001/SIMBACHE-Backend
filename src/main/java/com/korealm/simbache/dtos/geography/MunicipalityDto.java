package com.korealm.simbache.dtos.geography;

import com.korealm.simbache.models.Municipality;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class MunicipalityDto {
    public MunicipalityDto(Municipality municipality) {
        this.municipalityId = municipality.getMunicipalityId();
        this.municipalityName = municipality.getMunicipalityName();
    }

    @NotBlank
    private short municipalityId;

    @NotBlank
    private String municipalityName;
}
