package com.korealm.simbache.dtos.geography;

import com.korealm.simbache.models.Street;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class StreetDto {
    public StreetDto(Street street) {
        this.streetId = street.getStreetId();
        this.streetName = street.getStreetName();
    }

    @NotBlank
    private Long streetId;

    @NotBlank
    private String streetName;
}
