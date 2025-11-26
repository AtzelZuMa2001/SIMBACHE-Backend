package com.korealm.simbache.dtos.geography;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZipCodeLookupDto {
    private Short stateId;
    private Short municipalityId;
    private Integer localityId;
    private Integer postalCode;

    // Optional: Include names for better context
    private String stateName;
    private String municipalityName;
    private String localityName;
}