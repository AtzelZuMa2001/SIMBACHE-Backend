package com.korealm.simbache.dtos.citizens;

import com.korealm.simbache.models.Citizens;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenResponseDto {

    private Long citizenId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private String email;
    private Long phoneNumber;
    private LocalDateTime createdAt;
    private CitizenLocationDto registeredLocation;

    public CitizenResponseDto(Citizens entity) {
        this.citizenId = entity.getCitizenId();
        this.firstName = entity.getFirstName();
        this.middleName = entity.getMiddleName();
        this.lastName = entity.getLastName();
        this.secondLastName = entity.getSecondLastName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.createdAt = entity.getCreatedAt();

        if (entity.getRegisteredLocation() != null) {
            var loc = entity.getRegisteredLocation();
            this.registeredLocation = CitizenLocationDto.builder()
                    .locationId(loc.getLocationId())
                    .stateName(loc.getState() != null ? loc.getState().getStateName() : null)
                    .municipalityName(loc.getMunicipality() != null ? loc.getMunicipality().getMunicipalityName() : null)
                    .localityName(loc.getLocality() != null ? loc.getLocality().getLocalityName() : null)
                    .build();
        }
    }
}