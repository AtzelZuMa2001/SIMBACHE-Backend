package com.korealm.simbache.dtos.geography;

import com.korealm.simbache.models.Locality;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LocalityDto {
    public LocalityDto(Locality locality) {
        this.localityId = locality.getLocalityId();
        this.localityName = locality.getLocalityName();
    }

    @NotBlank
    private int localityId;

    @NotBlank
    private String localityName;
}
