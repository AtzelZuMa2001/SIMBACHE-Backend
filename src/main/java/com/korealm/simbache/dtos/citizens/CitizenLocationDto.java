package com.korealm.simbache.dtos.citizens;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenLocationDto {
    private Long locationId;
    private String stateName;
    private String municipalityName;
    private String localityName;
}