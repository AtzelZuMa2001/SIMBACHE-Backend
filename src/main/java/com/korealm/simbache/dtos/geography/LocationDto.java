package com.korealm.simbache.dtos.geography;

import com.korealm.simbache.models.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationDto {
    public LocationDto(Location l) {
        this.locationId = l.getLocationId();
        this.stateId = l.getState().getStateId();
        this.municipalityId = l.getMunicipality().getMunicipalityId();
        this.localityId = l.getLocality().getLocalityId();
        this.mainStreetId = l.getMainStreet().getStreetId();
        this.streetOneId = l.getStreetOne() == null ? null : l.getStreetOne().getStreetId();
        this.streetTwoId = l.getStreetTwo() == null ? null : l.getStreetTwo().getStreetId();
    }

    private Long locationId;
    private short stateId;
    private short municipalityId;
    private int localityId;
    private Long mainStreetId;
    private Long streetOneId; // nullable
    private Long streetTwoId; // nullable
}
