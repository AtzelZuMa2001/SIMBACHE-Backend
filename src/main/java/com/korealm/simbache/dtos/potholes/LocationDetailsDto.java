package com.korealm.simbache.dtos.potholes;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetailsDto {
    private String stateName;
    private String municipalityName;
    private String localityName;
    private String coloniaName; // nullable
    private int postalCode;
    private String mainStreetName;
    private String streetOneName; // nullable
    private String streetTwoName; // nullable
}
